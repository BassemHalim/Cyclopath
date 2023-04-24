package com.bassemHalim.cyclopath.FIT;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import java.util.Collection;
import java.util.Map;
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
public class FITGarminDownloader implements FITDownloader {
    private String Username;
    private String Password;
    private String access_token;
    private String garmin_connect_base_url = "https://connect.garmin.com";
    private String garmin_connect_enus_url = garmin_connect_base_url + "/en-US";
    private String garmin_connect_sso_base_url = "https://sso.garmin.com/sso/";

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
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.route("https://connect.garmin.com/activitylist-service/**", route -> {
                System.out.println(route.request().url());
                System.out.println(route.request().headers());
                route.resume();
            });

            Response response = page.navigate("https://connect.garmin.com/signin/");
            page.waitForTimeout(100 + Math.random() * 1000);
            FrameLocator signinFrame = page.frameLocator("#gauth-widget-frame-gauth-widget");
            signinFrame.locator("#username").fill(this.Username);
            page.waitForTimeout(100 + Math.random() * 1000);
            signinFrame.locator("#password").fill(this.Password);
            page.waitForTimeout(100 + Math.random() * 1000);
            signinFrame.locator("#login-btn-signin").click();
            page.waitForLoadState();
            page.waitForTimeout(10000);

            String json = page.context().storageState();
            Pattern pattern = Pattern.compile("access_token"); // str" "access_token\":\""
            Matcher matcher = pattern.matcher(json);
            if (!matcher.find()) {

                throw new RuntimeException("didn't find access token");
            }
            this.access_token = json.substring(matcher.end() + 5, matcher.end() + 1137);
            System.out.println(this.access_token);
//            System.out.println(json);// => {"cookies":[{"name":"SESSION","value":"90fb93d4-9b31-476f-90b5-ce3db4aea017","domain":"sso.garmin.com","path":"/sso/","expires":-1,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"__cflb","value":"02DiuJLbVZHipNWxN8xjNziif9XwiLsQdKUiEK4SdPrzg","domain":"connect.garmin.com","path":"/","expires":1682457223.221704,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"__cfruid","value":"92693512d3d207321c6721bbad32e9aaa233799b-1682374423","domain":".connect.garmin.com","path":"/","expires":-1,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"GarminUserPrefs","value":"en-US","domain":".garmin.com","path":"/","expires":-1,"httpOnly":false,"secure":false,"sameSite":"Lax"},{"name":"__cf_bm","value":"WIt9xBssjtwDojfxRCRLjsZxoZj4lcDECeYeVrI2kU4-1682374423-0-AYJB21H0judBHNSYQiqv0rrBHOFn7ryuhJhIrtA62x7XtqdVYQwe8OHQia1HWCcCDW8aEBtJe9Fipmshs3gSnI0=","domain":".sso.garmin.com","path":"/","expires":1682376223.463019,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"_cfuvid","value":"vHvCJKZdRhDUTwZltf104v5QPUYSqUDjZz.3cvGQHLM-1682374423449-0-604800000","domain":".sso.garmin.com","path":"/","expires":-1,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"notice_behavior","value":"none","domain":".connect.garmin.com","path":"/","expires":1716502430,"httpOnly":false,"secure":true,"sameSite":"None"},{"name":"__VCAP_ID__","value":"4928d274-5fc8-405a-6a42-9f62","domain":"sso.garmin.com","path":"/","expires":-1,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"__cflb","value":"0H28vqK2vhBdjKAHtwTJuV59urJCcVoJDSbMx9WUMZn","domain":"sso.garmin.com","path":"/","expires":1682457223.641396,"httpOnly":true,"secure":false,"sameSite":"Lax"},{"name":"org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE","value":"en_US","domain":"sso.garmin.com","path":"/","expires":-1,"httpOnly":false,"secure":false,"sameSite":"Lax"},{"name":"CASTGC","value":"TGT-1565433-Zm51g2aFYW5BAhCX1TigRN1tkv91Z6wKh0wJDqdTsDT9zBJENL-cas","domain":"sso.garmin.com","path":"/","expires":-1,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"GARMIN-SSO","value":"1","domain":".garmin.com","path":"/","expires":-1,"httpOnly":false,"secure":true,"sameSite":"None"},{"name":"GarminNoCache","value":"true","domain":".garmin.com","path":"/","expires":-1,"httpOnly":false,"secure":false,"sameSite":"Lax"},{"name":"GARMIN-SSO-GUID","value":"A8AC6CD402B3A4802CEFB85231EA67B1478E2BCA","domain":".garmin.com","path":"/","expires":-1,"httpOnly":false,"secure":false,"sameSite":"Lax"},{"name":"GARMIN-SSO-CUST-GUID","value":"e6dc3fea-aa4c-4680-8579-74cf79daa889","domain":".garmin.com","path":"/","expires":-1,"httpOnly":false,"secure":false,"sameSite":"Lax"},{"name":"__cfruid","value":"dcd8a39b86bcbe3d0f4da17432fa4133ed6e48d0-1682374427","domain":".sso.garmin.com","path":"/","expires":-1,"httpOnly":true,"secure":true,"sameSite":"None"},{"name":"SameSite","value":"None","domain":"connect.garmin.com","path":"/","expires":1682374461.773612,"httpOnly":false,"secure":true,"sameSite":"Lax"},{"name":"SESSIONID","value":"MTYzY2ViNWYtODE1Zi00NDIyLTg1OGEtZjM0YWE1NDA2NTZl","domain":"connect.garmin.com","path":"/","expires":-1,"httpOnly":true,"secure":true,"sameSite":"Lax"},{"name":"JWT_FGP","value":"a10c11c7-52a6-481c-894e-6f3576bd33ff","domain":".connect.garmin.com","path":"/","expires":1682381629.086988,"httpOnly":true,"secure":true,"sameSite":"Strict"},{"name":"ADRUM_BTa","value":"R:34|g:e0768432-cdaf-4f78-a211-3f7799fe2784|n:garmin_869629ee-d273-481d-b5a4-f4b0a8c4d5a3","domain":"connect.garmin.com","path":"/","expires":1682374461.773601,"httpOnly":false,"secure":true,"sameSite":"Lax"},{"name":"ADRUM_BT1","value":"R:34|i:1292310|e:5","domain":"connect.garmin.com","path":"/","expires":1682374461.773628,"httpOnly":false,"secure":true,"sameSite":"Lax"},{"name":"G_ENABLED_IDPS","value":"google","domain":".connect.garmin.com","path":"/","expires":1716934431.780803,"httpOnly":false,"secure":false,"sameSite":"Lax"}],"origins":[{"origin":"https://connect.garmin.com","localStorage":[{"name":"token","value":"{\"scope\":\"GOLF_API_READ GHS_HID ATP_READ GHS_SAMD INSIGHTS_READ CONNECT_WRITE DIVE_SHARED_READ GHS_REGISTRATION GOLF_API_WRITE INSIGHTS_WRITE PRODUCT_SEARCH_READ GOLF_SHARED_READ CONNECT_NON_SOCIAL_SHARED_READ CONNECT_READ ATP_WRITE\",\"jti\":\"5930ac80-4afa-4c61-9124-9406e14292d8\",\"access_token\":\"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImRpLW9hdXRoLXNpZ25lci1wcm9kLTIwMjIifQ.eyJzY29wZSI6WyJBVFBfUkVBRCIsIkFUUF9XUklURSIsIkNPTk5FQ1RfTk9OX1NPQ0lBTF9TSEFSRURfUkVBRCIsIkNPTk5FQ1RfUkVBRCIsIkNPTk5FQ1RfV1JJVEUiLCJESVZFX1NIQVJFRF9SRUFEIiwiR0hTX0hJRCIsIkdIU19SRUdJU1RSQVRJT04iLCJHSFNfU0FNRCIsIkdPTEZfQVBJX1JFQUQiLCJHT0xGX0FQSV9XUklURSIsIkdPTEZfU0hBUkVEX1JFQUQiLCJJTlNJR0hUU19SRUFEIiwiSU5TSUdIVFNfV1JJVEUiLCJQUk9EVUNUX1NFQVJDSF9SRUFEIl0sImlzcyI6Imh0dHBzOi8vZGlhdXRoLmdhcm1pbi5jb20iLCJleHAiOjE2ODIzNzgwMjksImlhdCI6MTY4MjM3NDQyOSwiZ2FybWluX2d1aWQiOiJlNmRjM2ZlYS1hYTRjLTQ2ODAtODU3OS03NGNmNzlkYWE4ODkiLCJqdGkiOiI1OTMwYWM4MC00YWZhLTRjNjEtOTEyNC05NDA2ZTE0MjkyZDgiLCJjbGllbnRfaWQiOiJDT05ORUNUX1dFQiIsImZncCI6ImE2NzU4OTdjMDkwOTE2ZDk4ZmQ1ZTFlZmQ0MDk2ZGZiMTZiZTlkZWM1ZWYyMzc0ODI0ZWExYmNmZDE2MTZjNGUifQ.a7Q_HrvHqwNBlDmtcyCTI47m-qHBbH5E1L8RcXLGLasp_K1QxT7Q18G1eIeVgbWoJEa5vgDz3QUBU3cqond68iklVHgK60VBAbQIEpiNLjxCCql1zsw7HUOM1WCp2E3zDfKGb-H4RFHKMDIQkiUiaxFWppI4KdrM-u6paKKnfLrxdXEwtf2MlEvwVwEUoJ0Ikk6r9Y4-to7MLbWvO2pzqiQo0T2XARxeHG9iuMF2yK_OaJKAnGPMu5d8RpOzeCTjhtF-wQjiA9vh3uD2fWbQy-raUtR8_aLCiX-H8nGlJ-yYsTjjXegNeFeOuIOSue88jyrxEmNiUjEy-4OKnk5iUg\",\"token_type\":\"Bearer\",\"refresh_token\":\"f70efb5d-cabf-4d1b-b4ad-0cd15d1d9dbc\",\"expires_in\":3599,\"refresh_token_expires_in\":7199,\"expires\":1682377969091,\"refresh_token_expires\":1682381569091}"},{"name":"pref-101798589","value":"{\"LAST_DASHBOARD\":65503026}"}]}]}

            page.waitForTimeout(200 + Math.random() * 1000);
            System.out.println(context.storageState());
            // get activity list
            System.out.println("getting activities");
            APIResponse response1 = context.request().get("https://connect.garmin.com/activitylist-service/activities/search/activities", RequestOptions.create()
                    .setQueryParam("limit", 20)
                    .setQueryParam("start", 20)
                    .setHeader("authorization", "Bearer " + this.access_token)
                    .setHeader("di-backend", "connectapi.garmin.com")
                    .setHeader("dnt", "1")
                    .setHeader("nk", "NT")
                    .setHeader("referer", "https://connect.garmin.com/modern/activities")
                    .setHeader("x-requested-with", "XMLHttpRequest")

            );

            if (response1.ok()) {
                System.out.println(response1.text());
            } else {
                System.out.println(response1.status());
            }
            page.waitForTimeout(12345678);

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

