package com.bassemHalim.cyclopath.Activity;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public interface ActivityDownloader {
    boolean login();

    List<Activity> getActivitiesList() throws IOException;

    void downloadActivity(Activity activity);

}
