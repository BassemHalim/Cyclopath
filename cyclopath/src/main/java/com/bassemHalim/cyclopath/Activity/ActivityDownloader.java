package com.bassemHalim.cyclopath.Activity;

import com.google.gson.JsonObject;
import io.jenetics.jpx.GPX;

import java.io.IOException;
import java.util.List;

public interface ActivityDownloader {
    boolean login();

    List<Activity> getActivitiesList();

    GPX downloadActivity(Long id);

}
