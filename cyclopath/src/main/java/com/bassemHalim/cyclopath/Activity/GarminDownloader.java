package com.bassemHalim.cyclopath.Activity;


import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a FIT data downloader from garmin connect
 * the downloader mimics a Browser since garmin doesn't provide access to its API
 * the drawback is that the username and password has to be provided :(
 *
 * @author Bassem Halim
 * @see <a href="https://github.com/tcgoetz/GarminDB/blob/master/garmindb/download.py">python reference repo</a>
 */
@Service
public class GarminDownloader implements ActivityDownloader {
    @Value("${Garmin.USERNAME}")
    private String Username;
    @Value("${Garmin.PASSWORD}")
    private String Password;
    private String access_token;
    private final String garmin_connect_base_url = "https://connect.garmin.com";
    private final String garmin_activity_list_url = "https://connect.garmin.com/activitylist-service/activities/search/activities";
    private final String garmin_activity_download_gpx_url = "https://connect.garmin.com/download-service/export/gpx/activity/";

    private Page page;
    private BrowserContext context;
    private Playwright playwright = Playwright.create();

    private Browser browser;
    private String state;

    public GarminDownloader() {
    }

    public GarminDownloader(String username, String password) {
        Username = username;
        Password = password;
    }

    @Override
    public boolean login() {
        // Login
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setSlowMo(0).setHeadless(true));
        context = browser.newContext();
        this.page = context.newPage();

        // sign in
        System.out.println("signing in");
        Response response = page.navigate("https://connect.garmin.com/signin/");
        page.waitForTimeout(100 + Math.random() * 100);
        FrameLocator signinFrame = page.frameLocator("#gauth-widget-frame-gauth-widget");
        signinFrame.locator("#username").fill(this.Username);
        page.waitForTimeout(100 + Math.random() * 100);
        signinFrame.locator("#password").fill(this.Password);
        page.waitForTimeout(100 + Math.random() * 100);
        signinFrame.locator("#login-btn-signin").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
//        page.waitForTimeout(2000);
        System.out.println("getting access token");
        // get access token
        String json = page.context().storageState();
        Pattern pattern = Pattern.compile("access_token"); // str" "access_token\":\""
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return false;
        }
        this.access_token = json.substring(matcher.end() + 5, matcher.end() + 1137);
        this.state = context.storageState();
        System.out.println(access_token);
        return true;
    }

    @Override
    public List<Activity> getActivitiesList() throws IOException {
        List<Activity> activityList = new ArrayList();
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
            Files.write(Paths.get("activities_samples/list.json"), response.body());
            Type activityListType = new TypeToken<ArrayList<Activity>>() {
            }.getType();
            activityList = new GsonBuilder().create().fromJson(json, activityListType);
        } else {
            System.out.println(response.status());
        }
//        browser.close();
        return activityList;
    }

    @Override
    public void downloadActivity(Activity activity) {
        Long id = activity.getActivityId();
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
        Path path = Paths.get("activities_samples/" + id + ".gpx");
        try {
            Files.write(path, response.body());
        } catch (IOException e) {
            throw new RuntimeException("couldn't save file to:" + path);
        }
    }
}

