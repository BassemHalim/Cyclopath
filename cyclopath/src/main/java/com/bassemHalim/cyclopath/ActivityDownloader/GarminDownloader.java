package com.bassemHalim.cyclopath.ActivityDownloader;


import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.ActivityDownloader.GarminActivityDTO.ActivityDTOMapper;
import com.bassemHalim.cyclopath.ActivityDownloader.GarminActivityDTO.GarminActivityDTO;
import com.bassemHalim.cyclopath.ActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.cyclopath.geoJSON.geoJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;


/**
 * a GPX data downloader from garmin connect
 * the downloader mimics a Browser since garmin doesn't provide access to its API
 * the drawback is that the username and password has to be provided :(
 *
 * @author Bassem Halim
 * @see <a href="https://github.com/tcgoetz/GarminDB/blob/master/garmindb/download.py">python reference repo</a>
 */
@Service
public class GarminDownloader implements ActivityDownloader {


    @Value("${Garmin.USERNAME}")
    @NotNull
    @Email
    private String Username;
    @Value("${Garmin.PASSWORD}")
    @NotNull
    private String Password;
    private String access_token;
    private long access_token_expiry;
    // @TODO clean urls
    private final String garmin_connect_signin_url = "https://connect.garmin.com/signin";
    private final String garmin_activity_list_url = "https://connect.garmin.com/activitylist-service/activities/search/activities";
    private final String garmin_activity_download_gpx_url = "https://connect.garmin.com/download-service/export/gpx/activity/";
    private final String garmin_activity_service_url = "https://connect.garmin.com/activity-service/activity/";
    private Page page;
    private BrowserContext context;
    private final Playwright playwright = Playwright.create();

    private Browser browser;
    private String state;
    @Autowired
    private ActivityDTOMapper DTOmapper;

    private boolean login() {
        // Login
        Path statePath = Paths.get("state.json");
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setSlowMo(100).setHeadless(true));
        BrowserContext context = browser.newContext();
        if (Files.exists(statePath)) {
            context = browser.newContext(
                    new Browser.NewContextOptions().setStorageStatePath(statePath));
        }

        this.page = context.newPage();

        // sign in
        // check if saved tokens are valid
        if (!getAccessToken(page.context().storageState())) {
            System.out.println("signing in");
            Response response = page.navigate(this.garmin_connect_signin_url);
            page.waitForTimeout(Math.random() * 100);
            if (!response.ok()) {
                return false;
            }
//        FrameLocator signinFrame = page.frameLocator("#gauth-widget-frame-gauth-widget");
            page.locator("#email").waitFor();
            page.locator("#email").fill(this.Username);
            page.waitForTimeout(100 + Math.random() * 100);
            page.locator("#password").fill(this.Password);
            page.waitForTimeout(100 + Math.random() * 100);
            page.getByTestId("g__button").click();
//        page.locator("#login-btn-signin").click();
            page.locator("#column-0").waitFor(); // wait for page to load
            System.out.println("getting access token");
            // get access token
            String storageState = page.context().storageState();
            if (!getAccessToken(storageState)) return false;
            this.state = context.storageState();

//            System.out.println(this.state);
            context.storageState(new BrowserContext.StorageStateOptions().setPath(statePath));
            page.close();
        }
        return true;
    }

    private boolean getAccessToken(String storageState) {
        this.state = storageState;
        Pattern pattern = Pattern.compile("access_token"); // str" \"access_token\":\""
        Pattern pattern1 = Pattern.compile("access_token.{5}[a-zA-z0-9.-]*\"");
        Matcher matcher = pattern1.matcher(storageState); // @FIXME
        if (!matcher.find()) return false;

        this.access_token = storageState.substring(matcher.start() + 17, matcher.end() - 2);
//        this.access_token = storageState.substring(matcher.end() + 5, matcher.end() + 1137);
//        System.out.println(this.access_token);
//        return false;
        pattern = Pattern.compile(
                "refresh_token_expires_in\\\\\\\"\\:[0-9]{4},\\\\\\\"expires\\\\\\\"\\:"
        ); // str" "refresh_token_expires_in\":7199,\"expires\":1682571122104
        matcher = pattern.matcher(storageState);
        if (!matcher.find()) return false;
        this.access_token_expiry = Long.parseLong(
                storageState.substring(matcher.end(), matcher.end() + 13));
        return signedIn(); // verify token is still valid
    }

    private boolean signedIn() {
        return this.access_token_expiry > Instant.now().toEpochMilli();
    }

    @Override
    public List<ActivityListItemDTO> getActivitiesList() {
        if (!login()) {
            System.out.println("failed to authenticate");
        }
//        System.out.println(access_token);

        List<ActivityListItemDTO> activityList = new ArrayList<>();
        // get activity list
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setStorageState(state));
        APIResponse response = context.request().get(this.garmin_activity_list_url, RequestOptions.create()
                .setQueryParam("limit", 1000)
                .setQueryParam("start", 0)
                .setHeader("authorization", "Bearer " + this.access_token)
                .setHeader("di-backend", "connectapi.garmin.com")
                .setHeader("dnt", "1")
                .setHeader("nk", "NT")
                .setHeader("referer", "https://connect.garmin.com/modern/activities")
                .setHeader("x-requested-with", "XMLHttpRequest")
        );
        if (response.ok()) {
            String json = response.text();
//            System.out.println(json);
            try {
                Files.write(Paths.get("activities_samples/list.json"), response.body());
            } catch (Exception e) {
                System.out.println(e);
            }
            Type activityListType = new TypeToken<ArrayList<ActivityListItemDTO>>() { //@FIXME
            }.getType();
            activityList = new GsonBuilder().create().fromJson(json, activityListType);

        } else {
            System.out.println("couldn't retrieve activity list status:" + response.status());
        }
//        browser.close();
        return activityList;
    }

    @Override
    public Activity getActivity(@NotNull @Positive Long ID) {

        if (!signedIn()) {
            if (!login()) {
                throw new RuntimeException("failed to login");
            }
        }
        System.out.println("downloading Activity: " + ID);
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setStorageState(state)
                .setBaseURL(this.garmin_activity_service_url)
        );
        APIResponse response = context.request().get(ID.toString(), RequestOptions.create()
                .setHeader("authorization", "Bearer " + this.access_token)
                .setHeader("di-backend", "connectapi.garmin.com")
                .setHeader("dnt", "1")
                .setHeader("nk", "NT")
                .setHeader("referer", "https://connect.garmin.com/modern/activity/" + ID)
                .setHeader("x-app-ver", "4.65.3.0")
                .setHeader("x-requested-with", "XMLHttpRequest")
        );

        if (!response.ok()) {
            throw new RuntimeException(response.statusText() + "couldn't download activity file");
        }
        String json = response.text();
//        String json = "{\"activityId\":8507418985,\"activityUUID\":{\"uuid\":\"c01e84ca-115c-440a-91a0-504f592b2834\"},\"activityName\":\"Marin County Cycling\",\"userProfileId\":101798589,\"isMultiSportParent\":false,\"activityTypeDTO\":{\"typeId\":2,\"typeKey\":\"cycling\",\"parentTypeId\":17,\"isHidden\":false,\"restricted\":false,\"trimmable\":true},\"eventTypeDTO\":{\"typeId\":9,\"typeKey\":\"uncategorized\",\"sortOrder\":10},\"accessControlRuleDTO\":{\"typeId\":2,\"typeKey\":\"private\"},\"timeZoneUnitDTO\":{\"unitId\":121,\"unitKey\":\"America/Los_Angeles\",\"factor\":0.0,\"timeZone\":\"America/Los_Angeles\"},\"metadataDTO\":{\"isOriginal\":true,\"deviceApplicationInstallationId\":894696,\"agentApplicationInstallationId\":null,\"agentString\":null,\"fileFormat\":{\"formatId\":7,\"formatKey\":\"fit\"},\"associatedCourseId\":null,\"lastUpdateDate\":\"2022-11-26T04:13:16.0\",\"uploadedDate\":\"2022-03-23T02:34:39.0\",\"videoUrl\":null,\"hasPolyline\":true,\"hasChartData\":true,\"hasHrTimeInZones\":true,\"hasPowerTimeInZones\":false,\"userInfoDto\":{\"userProfilePk\":101798589,\"displayname\":\"7472e467-1faf-4e23-bc92-8af59c386f6b\",\"fullname\":\"Bassem\",\"profileImageUrlLarge\":null,\"profileImageUrlMedium\":\"https://s3.amazonaws.com/garmin-connect-prod/profile_images/30fba2f4-3c23-48c4-8bed-506f7d72fbd5-101798589.png\",\"profileImageUrlSmall\":\"https://s3.amazonaws.com/garmin-connect-prod/profile_images/e247b616-4d40-46b4-9f55-c893b0fe32a7-101798589.png\",\"userPro\":false},\"childIds\":[],\"childActivityTypes\":[],\"sensors\":[],\"activityImages\":[],\"manufacturer\":\"GARMIN\",\"diveNumber\":null,\"lapCount\":13,\"associatedWorkoutId\":null,\"isAtpActivity\":null,\"deviceMetaDataDTO\":{\"deviceId\":\"3352844860\",\"deviceTypePk\":36817,\"deviceVersionPk\":894696},\"hasIntensityIntervals\":false,\"hasSplits\":false,\"eBikeMaxAssistModes\":null,\"eBikeBatteryUsage\":null,\"eBikeBatteryRemaining\":null,\"eBikeAssistModeInfoDTOList\":null,\"calendarEventInfo\":null,\"autoCalcCalories\":false,\"favorite\":false,\"manualActivity\":false,\"runPowerWindDataEnabled\":null,\"trimmed\":false,\"personalRecord\":false,\"gcj02\":false,\"elevationCorrected\":true},\"summaryDTO\":{\"startTimeLocal\":\"2022-03-22T13:10:20.0\",\"startTimeGMT\":\"2022-03-22T20:10:20.0\",\"startLatitude\":37.83193412236869,\"startLongitude\":-122.48024803586304,\"distance\":97854.03,\"duration\":17125.906,\"movingDuration\":16849.0,\"elapsedDuration\":22948.664,\"elevationGain\":422.73,\"elevationLoss\":475.33,\"maxElevation\":67.6,\"minElevation\":-6.2,\"averageSpeed\":5.714000225067138,\"averageMovingSpeed\":5.8077055759985745,\"maxSpeed\":11.710000038146973,\"calories\":2477.0,\"bmrCalories\":379.0,\"averageHR\":154.0,\"maxHR\":185.0,\"endLatitude\":37.808159943670034,\"endLongitude\":-122.42804076522589,\"maxVerticalSpeed\":3.799999952316284,\"waterEstimated\":1734.0,\"minActivityLapDuration\":263.213},\"locationName\":\"Marin County\"}\n";
//        System.out.println(json);
        GarminActivityDTO activityDTO = new GsonBuilder().create().fromJson(json, GarminActivityDTO.class);
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
//        modelMapper.getConfiguration().setSkipNullEnabled(true);
//        Activity activity = modelMapper.map(activityDTO, Activity.class);
//        ActivityListItemDTO dto = modelMapper.map(activityDTO, ActivityListItemDTO.class);
        Activity activity = DTOmapper.apply(activityDTO);
        return activity;

    }

    /**
     * download the GPX (GPS) file containing the activity route then convert it to geoJSON and
     * compress it
     *
     * @param id activity id
     * @return returns the gzipped geoJSON route
     */
    @Override
    public byte[] downloadActivityRoute(@NotNull @Positive Long id) {

        String filePath = "activities_samples/" + id + ".gpx";
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            if (!signedIn()) {
                if (!login()) {
                    throw new RuntimeException("failed to login");
                }
            }
            System.out.println("downloading: " + id);
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setStorageState(state)
                    .setBaseURL(this.garmin_activity_download_gpx_url)
            );
            APIResponse response = context.request().get(id.toString(), RequestOptions.create()
                    .setHeader("authorization", "Bearer " + this.access_token)
                    .setHeader("di-backend", "connectapi.garmin.com")
                    .setHeader("dnt", "1")
                    .setHeader("nk", "NT")
                    .setHeader("referer", "https://connect.garmin.com/modern/activity/" + id)
                    .setHeader("x-app-ver", "4.65.3.0")
            );

            if (!response.ok()) {
                throw new RuntimeException(response.statusText() + "couldn't download activity file");
            }
            try {
                Files.write(path, response.body());
            } catch (IOException e) {
                throw new RuntimeException("couldn't save file to:" + path);
            }
        }
        try {
            String gpx = Files.readString(path);
            geoJSON file = new geoJSON();
            geoJSON geojson = file.GPXtoGeoJson(gpx);
            ObjectMapper mapper = new ObjectMapper();

            return gzip(mapper.writeValueAsBytes(geojson));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private byte[] gzip(byte[] data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream out = new GZIPOutputStream(bos);
            out.write(data);
            out.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

