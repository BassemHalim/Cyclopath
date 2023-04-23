package com.bassemHalim.cyclopath.FIT;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
    private String garmin_connect_sso_signin_url = garmin_connect_sso_base_url + "signin";
    private String garmin_sso_params_url = "https://sso.garmin.com/sso/signin?service=https%3A%2F%2Fconnect.garmin.com%2Fmodern%2F&webhost=https%3A%2F%2Fconnect.garmin.com%2Fmodern%2F&source=https%3A%2F%2Fconnect.garmin.com%2Fsignin%2F&redirectAfterAccountLoginUrl=https%3A%2F%2Fconnect.garmin.com%2Fmodern%2F&redirectAfterAccountCreationUrl=https%3A%2F%2Fconnect.garmin.com%2Fmodern%2F&gauthHost=https%3A%2F%2Fsso.garmin.com%2Fsso&locale=en_US&id=gauth-widget&cssUrl=https%3A%2F%2Fconnect.garmin.com%2Fgauth-custom-v1.2-min.css&privacyStatementUrl=https%3A%2F%2Fwww.garmin.com%2Fen-US%2Fprivacy%2Fconnect%2F&clientId=GarminConnect&rememberMeShown=true&rememberMeChecked=false&createAccountShown=true&openCreateAccount=false&displayNameShown=false&consumeServiceTicket=false&initialFocus=true&embedWidget=false&socialEnabled=false&generateExtraServiceTicket=true&generateTwoExtraServiceTickets=true&generateNoServiceTicket=false&globalOptInShown=true&globalOptInChecked=false&mobile=false&connectLegalTerms=true&showTermsOfUse=false&showPrivacyPolicy=false&showConnectLegalAge=false&locationPromptShown=true&showPassword=true&useCustomHeader=false&mfaRequired=false&performMFACheck=false&rememberMyBrowserShown=true&rememberMyBrowserChecked=false";
    private String garmin_connect_login_url = garmin_connect_enus_url + "/signin";
    private String garmin_connect_css_url = "https://static.garmincdn.com/com.garmin.connect/ui/css/gauth-custom-v1.2-min.css";
    private String garmin_connect_privacy_url = "//connect.garmin.com/en-US/privacy";
    private String garmin_connect_user_profile_url = "proxy/userprofile-service/userprofile";
    private String garmin_connect_activity_search_url = "proxy/activitylist-service/activities/search/activities";
    private String garmin_connect_usersummary_url = "proxy/usersummary-service/usersummary";
//    https://connect.garmin.com/modern/proxy/usersummary-service/usersummary/hydration/allData/2019-11-29

//    garmin_headers = {'NK': 'NT'}

    public FITGarminDownloader(String username, String password) {
        Username = username;
        Password = password;
        login();
    }

    public void login() {
        // Login
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setSlowMo(100).setHeadless(false));
            Page page = browser.newPage();

            Response response = page.navigate("https://connect.garmin.com/signin/");
            page.waitForTimeout(2000);
            FrameLocator signinFrame = page.frameLocator("#gauth-widget-frame-gauth-widget");
            signinFrame.locator("#username").fill(this.Username);
            page.waitForTimeout(1234);
            signinFrame.locator("#password").fill(this.Password);
            page.waitForTimeout(100);
            signinFrame.locator("#login-btn-signin").click();
            page.waitForTimeout(102325);


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
