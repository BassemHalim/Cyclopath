package com.bassemHalim.cyclopath.Activity.ActivityDownloader;


import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityDTO.GarminActivityDTO;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.cyclopath.Activity.ActivityMapper;
import com.bassemHalim.cyclopath.User.UserService;
import com.bassemHalim.cyclopath.Utils.Compressor;
import com.bassemHalim.cyclopath.Weather.Weather;
import com.bassemHalim.cyclopath.geoJSON.geoJSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


/**
 * a GPX data downloader from garmin connect
 * the downloader mimics a Browser since garmin doesn't provide access to its API
 * the drawback is that the username and password has to be provided :(
 *
 * @author Bassem Halim
 * @see <a href="https://github.com/tcgoetz/GarminDB/blob/master/garmindb/download.py">python reference repo</a>
 */
@Service
@RequiredArgsConstructor
@Log
public class GarminDownloader implements ActivityDownloader {

    private static final String GARMIN_BASE_URL = "https://connect.garmin.com";
    private final String GARMIN_SIGNIN_URL = GARMIN_BASE_URL + "/signin";
    private final String GARMIN_ACTIVITY_LIST_URL = GARMIN_BASE_URL + "/activitylist-service/activities/search/activities";
    private final String GARMIN_ACTIVITY_GPX_URL = GARMIN_BASE_URL + "/download-service/export/gpx/activity/";
    private final String GARMIN_ACTIVITY_SERVICE_URL = GARMIN_BASE_URL + "/activity-service/activity/";
    @Value("${Garmin.USERNAME}")
    @NotNull
    @Email
    private String Username;
    @Value("${Garmin.PASSWORD}")
    @NotNull
    private String Password;
    private String access_token;
    private long access_token_expiry;
    private String state; // browser state: cookies + local storage
    private Page page;
    private final Browser browser;


    public boolean garminLogin() {
        // TODO: 5/16/2023 make faster without being detected as bot
        // Login
        log.info("Logging in to Garmin");
        Path statePath = Paths.get("state.json");
//        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setSlowMo(0).setHeadless(true));
        BrowserContext context = browser.newContext();
        if (Files.exists(statePath)) {
            context = browser.newContext(
                    new Browser.NewContextOptions().setStorageStatePath(statePath));
        }

        this.page = context.newPage();
        page.route("**/*.{png,jpg,jpeg}", route -> route.abort());
//        page.route("blob:https://connect.garmin.com/", route -> route.abort());
        // sign in
        // check if saved tokens are valid
        if (!getAccessToken(page.context().storageState())) {
//            log.info("Signing in to garmin connect");
            Response response = page.navigate(this.GARMIN_SIGNIN_URL);
            page.locator("#email").waitFor();
//            page.waitForURL("https://sso.garmin.com/portal/sso/en-US/sign-in?clientId=GarminConnect&service=https://connect.garmin.com/modern");
            // -------------------------------------------------------
            if (!response.ok()) {
                return false;
            }
//        FrameLocator signinFrame = page.frameLocator("#gauth-widget-frame-gauth-widget");
//            page.locator("#email").waitFor();
            page.locator("#email").fill(this.Username);
//            page.waitForTimeout( Math.random() * 100);
            page.locator("#password").fill(this.Password);
            page.waitForTimeout(Math.random() * 100);
            page.getByTestId("g__button").click();
//        page.locator("#garminLogin-btn-signin").click();
//            page.locator("#column-0").waitFor(); // wait for page to load @fixme very slow
            Request request = page.waitForRequest("https://connect.garmin.com/info-service/api/**",
                    () -> {
                    });

            //-------------------------------------------------------------
//            log.info("Getting garmin access token");
            // get access token
            String storageState = page.context().storageState();
            if (!getAccessToken(storageState)) throw new SecurityException("failed to login to garmin");
//            this.cookie = request.allHeaders().get("cookie");
//            this.access_token = request.allHeaders().get("authorization");
            this.state = context.storageState();
            context.storageState(new BrowserContext.StorageStateOptions().setPath(statePath));
            page.close();
        }

        log.info("Garmin login successful");
        return true;
    }

    private boolean getAccessToken(String storageState) {
        if (storageState == null) return false;
        this.state = storageState;
        Gson gson = new Gson();
        JsonElement stateJson = gson.fromJson(storageState, JsonElement.class);
        JsonArray origins = stateJson.getAsJsonObject().getAsJsonArray("origins");
        if (origins.isEmpty()) return false;
        JsonArray localStorageArray = origins.get(0)
                .getAsJsonObject()
                .getAsJsonArray("localStorage");
        JsonElement token = null;
        JsonObject token_params = null;
        for (JsonElement jsonElement : localStorageArray) {
            if (jsonElement.getAsJsonObject().get("name").getAsString().equals("token")) {
                token = jsonElement.getAsJsonObject().get("value");
                token_params = gson.fromJson(token.getAsJsonPrimitive().getAsString(), JsonObject.class);
                this.access_token = token_params.get("access_token").getAsString();
                this.access_token_expiry = token_params.get("expires").getAsLong();
            }
        }

        return tokenValid(); // verify token is still valid
    }

    private boolean tokenValid() {
        return access_token != null && this.access_token_expiry > Instant.now().toEpochMilli();
    }

    @Override
    public List<ActivityListItemDTO> getActivitiesList(int start, int limit) {
        if (!garminLogin()) {
            System.out.println("failed to authenticate");
        }

        List<ActivityListItemDTO> activityList = new ArrayList<>();
        // get activity list
        BrowserContext context = UserService.getCurrentUser().getBrowserContext();
        if (context == null) {
            context = browser.newContext(new Browser.NewContextOptions().setStorageState(state));
            UserService.getCurrentUser().setBrowserContext(context);
        }
        APIResponse response = context.request().get(this.GARMIN_ACTIVITY_LIST_URL, RequestOptions.create()
                .setQueryParam("limit", limit)
                .setQueryParam("start", start)
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
            Type activityListType = new TypeToken<ArrayList<ActivityListItemDTO>>() {
            }.getType();
            activityList = new GsonBuilder().create().fromJson(json, activityListType);

        } else {
            log.warning("couldn't retrieve activity list status:" + response.status());
        }
//        browser.close();
        return activityList;
    }

    @Override
    public Activity getActivity(@NotNull @Positive Long ID) {

        if (!tokenValid()) {
            if (!garminLogin()) {
                throw new RuntimeException("failed to garminLogin");
            }
        }

        log.info("downloading Activity: " + ID);
//        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
//                .setStorageState(state)
//                .setBaseURL(this.GARMIN_ACTIVITY_SERVICE_URL)
//        );
        BrowserContext context = UserService.getCurrentUser().getBrowserContext();
        if (context == null) {
            context = browser.newContext(new Browser.NewContextOptions().setStorageState(state));
            UserService.getCurrentUser().setBrowserContext(context);
        }
        APIResponse response = context.request().get(this.GARMIN_ACTIVITY_SERVICE_URL + ID.toString(), RequestOptions.create()
                .setHeader("authorization", "Bearer " + this.access_token)
                .setHeader("di-backend", "connectapi.garmin.com")
                .setHeader("dnt", "1")
                .setHeader("nk", "NT")
                .setHeader("referer", "https://connect.garmin.com/modern/activity/" + ID)
                .setHeader("x-app-ver", "4.67.0.16")
                .setHeader("x-requested-with", "XMLHttpRequest")
        );


        if (!response.ok()) {
            throw new RuntimeException(response.statusText() + "couldn't download activity file");
        }
        String json = response.text();
        GarminActivityDTO activityDTO = new GsonBuilder().create().fromJson(json, GarminActivityDTO.class);
        return ActivityMapper.MAPPER.toEntity(activityDTO);

    }

    /**
     * download the GPX (GPS) file containing the activity route then convert it to geoJSON and
     * compress it
     *
     * @param id activity id
     * @return returns the gzipped geoJSON route
     */
    @Override
    public byte[] getActivityRoute(@NotNull @Positive Long id) {
        if (!tokenValid()) {
            if (!garminLogin()) {
                throw new RuntimeException("failed to garminLogin");
            }
        }
        log.info("downloading GPX file for Activity: " + id);
//        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
//                .setStorageState(state)
//                .setBaseURL(this.GARMIN_ACTIVITY_GPX_URL)
//        );
        BrowserContext context = UserService.getCurrentUser().getBrowserContext();
        if (context == null) {
            context = browser.newContext(new Browser.NewContextOptions().setStorageState(state));
            UserService.getCurrentUser().setBrowserContext(context);
        }
        APIResponse response = context.request().get(GARMIN_ACTIVITY_GPX_URL + id.toString(), RequestOptions.create()
                .setHeader("authorization", "Bearer " + this.access_token)
                .setHeader("di-backend", "connectapi.garmin.com")
                .setHeader("dnt", "1")
                .setHeader("nk", "NT")
                .setHeader("referer", "https://connect.garmin.com/modern/activity/" + id)
                .setHeader("x-app-ver", "4.66.1.1")
        );

        if (!response.ok()) {
            throw new RuntimeException(response.statusText() + "couldn't download activity file");
        }
        String GPX = new String(response.body(), StandardCharsets.UTF_8);
        geoJSON json = new geoJSON();
        json.fromGPX(GPX);
        ObjectMapper mapper = new ObjectMapper();

        try {
            return Compressor.compress(mapper.writeValueAsBytes(json));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param id
     * @return
     * @Target_URL: https://connect.garmin.com/activity-service/activity/{ID}/weather
     */
    @Override
    public Weather getActivityWeather(@NotNull @Positive Long id) {
        if (!tokenValid()) {
            if (!garminLogin()) {
                throw new RuntimeException("failed to garminLogin");
            }
        }
        log.info("downloading Weather for Activity: " + id);

//        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
//                .setStorageState(state)
//                .setBaseURL(this.GARMIN_ACTIVITY_SERVICE_URL)
//        );
        BrowserContext context = UserService.getCurrentUser().getBrowserContext();
        if (context == null) {
            context = browser.newContext(new Browser.NewContextOptions().setStorageState(state));
            UserService.getCurrentUser().setBrowserContext(context);
        }
        APIResponse response = context.request().get(GARMIN_ACTIVITY_SERVICE_URL + id.toString() + "/weather", RequestOptions.create()
                .setHeader("authorization", "Bearer " + this.access_token)
                .setHeader("di-backend", "connectapi.garmin.com")
                .setHeader("dnt", "1")
                .setHeader("nk", "NT")
                .setHeader("referer", "https://connect.garmin.com/modern/activity/" + id)
                .setHeader("x-app-ver", "4.66.1.1")
        );

        if (!response.ok()) {
            throw new RuntimeException(response.statusText() + "couldn't download activity file");
        }
        String weatherStr = new String(response.body(), StandardCharsets.UTF_8);
        Weather weather = new GsonBuilder().create().fromJson(weatherStr, Weather.class);
        log.info("done downloading weather");
        return weather;
    }


}
