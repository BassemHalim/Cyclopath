package com.bassemHalim.Activity.ActivityDownloader;


import com.bassemHalim.Activity.Activity;
import com.bassemHalim.Activity.ActivityDownloader.GarminActivityDTO.GarminActivityDTO;
import com.bassemHalim.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.Activity.ActivityMapper;
import com.bassemHalim.Repositoy.SingleTableDB;
import com.bassemHalim.User.User;
import com.bassemHalim.User.UserService;
import com.bassemHalim.Utils.Compressor;
import com.bassemHalim.Weather.Weather;
import com.bassemHalim.geoJSON.geoJSON;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


/**
 * a GPX data downloader from garmin connect
 * the downloader mimics a Browser since garmin doesn't provide access to its API
 * the drawback is that the username and password has to be provided :(
 *
 * @author Bassem Halim
 */
@SuppressWarnings("FieldCanBeLocal")
@Service
@RequiredArgsConstructor
@Log
public class GarminDownloader implements ActivityDownloader {


    //	https://connect.garmin.com/modern/proxy/download-service/export/gpx/activity/11338406041
    private static String GARMIN_BASE_URL = "https://connect.garmin.com";
    private static String GARMIN_SIGNIN_URL = GARMIN_BASE_URL + "/signin";
    private static String GARMIN_ACTIVITY_LIST_URL = GARMIN_BASE_URL + "/activitylist-service/activities/search" +
            "/activities";
    private static String GARMIN_ACTIVITY_GPX_URL = GARMIN_BASE_URL + "/download-service/export/gpx" +
            "/activity/";
    private static String GARMIN_ACTIVITY_SERVICE_URL = GARMIN_BASE_URL + "/activity-service/activity/";
    @Value("${Garmin.USERNAME}")
    @NotNull
    @Email
    private String Username;
    @Value("${Garmin.PASSWORD}")
    @NotNull
    private String Password;
    private String access_token;
    private long access_token_expiry;
    private Page page;
    private final Browser browser;

    @Autowired
    private SingleTableDB repository;

    public boolean garminLogin() {
        // Login
        log.info("Logging in to Garmin");
//        Path statePath = Paths.get("state.json");
        BrowserContext context = browser.newContext();
//        if (useSavedToken && Files.exists(statePath)) {
//            context = browser.newContext(
//                    new Browser.NewContextOptions().setStorageStatePath(statePath));
//        }

        this.page = context.newPage();
        page.route("**/*.{png,jpg,jpeg}", route -> route.abort());
        // sign in
        // check if saved tokens are valid
        if (!getAccessToken(page.context().storageState())) {
            Response response = page.navigate(GARMIN_SIGNIN_URL);
            page.locator("#email").waitFor();
//            page.waitForURL("https://sso.garmin.com/portal/sso/en-US/sign-in?clientId=GarminConnect&service=https://connect.garmin.com/modern");
            // -------------------------------------------------------
            if (!response.ok()) {
                return false;
            }

            page.locator("#email").fill(this.Username);
            page.locator("#password").fill(this.Password);
            page.waitForTimeout(Math.random() * 100);
            page.getByTestId("g__button").click();
//        page.locator("#garminLogin-btn-signin").click();
            Request request = page.waitForRequest("https://connect.garmin.com/info-service/api/**", () -> {
            });

            //-------------------------------------------------------------
            // get access token
            String storageState = page.context().storageState();
            if (!getAccessToken(storageState)) throw new SecurityException("failed to login to garmin");
//            this.cookie = request.allHeaders().get("cookie");
//            this.access_token = request.allHeaders().get("authorization");
            User currentUser = UserService.getCurrentUser();
            currentUser.setBrowserState(context.storageState());
            repository.saveUser(currentUser);
//            context.storageState(new BrowserContext.StorageStateOptions().setPath(statePath));
            page.close();
        }

        log.info("Garmin login successful");
        return true;
    }

    private boolean getAccessToken(String storageState) {
        if (storageState == null) return false;
        User currentUser = UserService.getCurrentUser();
        currentUser.setBrowserState(storageState);
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
            log.info("failed to authenticate");
        }

        List<ActivityListItemDTO> activityList = new ArrayList<>();
        // get activity list
        User currentUser = UserService.getCurrentUser();
        BrowserContext context =
                browser.newContext(new Browser.NewContextOptions().setStorageState(currentUser.getBrowserState()));

        APIResponse response = context.request().get(GARMIN_ACTIVITY_LIST_URL, RequestOptions.create()
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
            Type activityListType = new TypeToken<ArrayList<ActivityListItemDTO>>() {
            }.getType();
            activityList = new GsonBuilder().create().fromJson(json, activityListType);
        } else {
            log.warning("couldn't retrieve activity list status:" + response.status());
        }
        return activityList;
    }

    @Override
    public Activity getActivity(@NotNull @Positive Long ID) {
        // @TODO use this url https://connect.garmin.com/activity-service/activity/{ID}/details
        if (!tokenValid()) {
            if (!garminLogin()) {
                throw new RuntimeException("failed to garminLogin");
            }
        }
        log.info("downloading Activity: " + ID);
        User currentUser = UserService.getCurrentUser();
        BrowserContext context =
                browser.newContext(new Browser.NewContextOptions().setStorageState(currentUser.getBrowserState()));

        APIResponse response = context.request()
                .get(GARMIN_ACTIVITY_SERVICE_URL + ID, RequestOptions.create()
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
        User currentUser = UserService.getCurrentUser();
        BrowserContext context =
                browser.newContext(new Browser.NewContextOptions().setStorageState(currentUser.getBrowserState()));

        APIResponse response = context.request().get(GARMIN_ACTIVITY_GPX_URL + id, RequestOptions.create()
                .setHeader("authorization", "Bearer " + this.access_token)
                .setHeader("di-backend", "connectapi.garmin.com")
                .setHeader("NK", "NT")
                .setHeader("referer", "https://connect.garmin" +
                        ".com/modern/activity/" + id)
                .setHeader("x-app-ver", "4.68.1.0")
        );

        if (!response.ok()) {
            log.warning("failed to fetch activity route for " + id);
            log.info(response.statusText());
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

    public String getActivityRoute2(@NotNull @Positive long activityID) {
        if (!tokenValid()) {
            if (!garminLogin()) {
                throw new RuntimeException("failed to garminLogin");
            }
        }
        log.info("downloading geoJSON for Activity: " + activityID);
        User currentUser = UserService.getCurrentUser();
        BrowserContext context =
                browser.newContext(new Browser.NewContextOptions().setStorageState(currentUser.getBrowserState()));

        APIResponse response = context.request()
                .get(GARMIN_ACTIVITY_SERVICE_URL + activityID + "/details?maxChartSize=2000&maxPolylineSize=4000",
                     RequestOptions.create()
                             .setHeader("authorization", "Bearer " + this.access_token)
                             .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                             .setHeader("Accept-Encoding", "gzip, deflate, br")
                             .setHeader("X-Requested-With", "XMLHttpRequest")
                             .setHeader("Connection", "keep-alive")
                             .setHeader("Sec-Fetch-Site", "same-origin")
                             .setHeader("Sec-Fetch-Mode", "cors")
                             .setHeader("Sec-Fetch-", "same-origin")
                             .setHeader("Sec-Fetch-Dest", "empty")

                             .setHeader("NK", "NT")
                             .setHeader("x-app-ver", "4.68.2.0")
                             .setHeader("di-backend", "connectapi.garmin.com")
                             .setHeader("referer", "https://connect.garmin" +
                                     ".com/modern/activity/" + activityID)
                );

        if (!response.ok()) {
            log.warning("failed to fetch activity route for " + activityID);
            log.info(response.text());
            throw new RuntimeException(response.statusText() + "couldn't download activity file");
        }
        String geoJSON = new String(response.body(), StandardCharsets.UTF_8);

        return geoJSON;
    }

    /**
     * @param id ActivityID
     * @return weather object
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

        User currentUser = UserService.getCurrentUser();
        BrowserContext context =
                browser.newContext(new Browser.NewContextOptions().setStorageState(currentUser.getBrowserState()));

        APIResponse response = context.request()
                .get(GARMIN_ACTIVITY_SERVICE_URL + id.toString() + "/weather", RequestOptions.create()
                        .setHeader("authorization", "Bearer " + this.access_token)
                        .setHeader("di-backend", "connectapi.garmin.com")
                        .setHeader("dnt", "1")
                        .setHeader("NK", "NT")
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

