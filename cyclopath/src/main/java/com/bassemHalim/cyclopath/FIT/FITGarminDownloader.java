package com.bassemHalim.cyclopath.FIT;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * a FIT data downloader from garmin connect
 * the downloader mimics a Browser since garmin doesn't provide access to its API
 * the drawback is that the username and password has to be provided :(
 *
 * @author Bassem Halim
 * @see <a href="https://github.com/tcgoetz/GarminDB/blob/master/garmindb/download.py">python reference repo</a>
 */
public class FITGarminDownloader implements FITDownloader {
    private String Username;
    private String Password;
    private String garmin_connect_base_url = "https://connect.garmin.com";
    private String garmin_connect_enus_url = garmin_connect_base_url + "/en-US";
    private String garmin_connect_sso_base_url = "https://sso.garmin.com/sso/";
    private String garmin_connect_sso_login_url = garmin_connect_sso_base_url + "signin";
    private String garmin_connect_login_url = garmin_connect_enus_url + "/signin";
    private String garmin_connect_css_url = "https://static.garmincdn.com/com.garmin.connect/ui/css/gauth-custom-v1.2-min.css";
    private String garmin_connect_privacy_url = "//connect.garmin.com/en-U/privacy";
    private String garmin_connect_user_profile_url = "proxy/userprofile-service/userprofile";
    private String garmin_connect_activity_search_url = "proxy/activitylist-service/activities/search/activities";
    private String garmin_connect_usersummary_url = "proxy/usersummary-service/usersummary";
//    https://connect.garmin.com/modern/proxy/usersummary-service/usersummary/hydration/allData/2019-11-29

//    garmin_headers = {'NK': 'NT'}

    public FITGarminDownloader(String username, String password) {
        Username = username;
        Password = password;
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            APIResponse response = page.request().get(this.garmin_connect_sso_login_url, RequestOptions.create()
                    .setQueryParam("service", this.garmin_connect_login_url + "/modern/")
                    .setQueryParam("webhost", this.garmin_connect_base_url)
                    .setQueryParam("source", this.garmin_connect_login_url)
                    .setQueryParam("redirectAfterAccountLoginUrl", this.garmin_connect_login_url + "/modern/")
                    .setQueryParam("redirectAfterAccountCreationUrl", this.garmin_connect_login_url + "/modern/")
                    .setQueryParam("gauthHost", this.garmin_connect_sso_base_url)
                    .setQueryParam("locale", "en_US")
                    .setQueryParam("id", "gauth-widget")
                    .setQueryParam("cssUrl", this.garmin_connect_css_url)
                    .setQueryParam("privacyStatementUrl", this.garmin_connect_privacy_url)
                    .setQueryParam("clientId", "GarminConnect")
                    .setQueryParam("rememberMeShown", "true")
                    .setQueryParam("rememberMeChecked", "false")
                    .setQueryParam("createAccountShown", "true")
                    .setQueryParam("openCreateAccount", "false")
                    .setQueryParam("displayNameShown", "false")
                    .setQueryParam("consumeServiceTicket", "false")
                    .setQueryParam("initialFocus", "true")
                    .setQueryParam("embedWidget", "false")
                    .setQueryParam("generateExtraServiceTicket", "true")
                    .setQueryParam("generateTwoExtraServiceTickets", "false")
                    .setQueryParam("generateNoServiceTicket", "false")
                    .setQueryParam("globalOptInShown", "true")
                    .setQueryParam("globalOptInChecked", "false")
                    .setQueryParam("mobile", "false")
                    .setQueryParam("connectLegalTerms", "true")
                    .setQueryParam("locationPromptShown", "true")
                    .setQueryParam("showPassword", "true"));
            if (response.ok()) {
//                System.out.println("title: " + response.text());
                // match " name="_csrf" value="" " from the html response
                Pattern pattern = Pattern.compile("name=\"_csrf\" value=\"");
                Matcher matcher = pattern.matcher(response.text());
                if (matcher.find()) {
                    int index = matcher.end();
//                    System.out.println(response.text().substring(index, index + 100));
                    String csrf = response.text().substring(index, index + 100);
                }
            }
        }

    }

    public boolean GarminLogin() {
        throw new UnsupportedOperationException("garmin fit downloader not implmented");
    }

    @Override
    public FIT downloadFIT() {
        throw new UnsupportedOperationException("garmin fit downloader not implmented");
    }
}
