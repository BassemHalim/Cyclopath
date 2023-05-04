package com.bassemHalim.cyclopath.Activity;


import com.bassemHalim.cyclopath.geoJSON.geoJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

//    @Autowired
//    private ActivityRepository activityRepository;

    @Value("${Garmin.USERNAME}")
    @NotNull
    @Email
    private String Username;
    @Value("${Garmin.PASSWORD}")
    @NotNull
    private String Password;
    private String access_token;
    private long access_token_expiry;
    private final String garmin_connect_signin_url = "https://connect.garmin.com/signin";
    private final String garmin_activity_list_url = "https://connect.garmin.com/activitylist-service/activities/search/activities";
    private final String garmin_activity_download_gpx_url = "https://connect.garmin.com/download-service/export/gpx/activity/";

    private Page page;
    private BrowserContext context;
    private final Playwright playwright = Playwright.create();

    private Browser browser;
    private String state;

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
    public List<Activity> getActivitiesList() {
        if (!login()) {
            System.out.println("failed to authenticate");
        }
//        System.out.println(access_token);

        List<Activity> activityList = new ArrayList<>();
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
            Type activityListType = new TypeToken<ArrayList<Activity>>() {
            }.getType();
            activityList = new GsonBuilder().create().fromJson(json, activityListType);

        } else {
            System.out.println("couldn't retrieve activity list status:" + response.status());
        }
//        browser.close();
        return activityList;
    }

    @Override
    public byte[] downloadActivity(@NotNull @Positive Long id) {

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

//            Files.write(Paths.get("activities_samples/" + id + ".json"), mapper.writeValueAsBytes(geojson));
//            StringToGzip(mapper.writeValueAsString(geojson), Paths.get("activities_samples/" + id + "_zip.json"));
            byte[] zipped = gzip(mapper.writeValueAsBytes(geojson));

            return zipped;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static byte[] gzip(byte[] data) {
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

