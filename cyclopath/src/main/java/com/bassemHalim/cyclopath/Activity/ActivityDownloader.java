package com.bassemHalim.cyclopath.Activity;

import com.bassemHalim.cyclopath.geoJSON.geoJSON;
import com.google.gson.JsonObject;
import io.jenetics.jpx.GPX;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public interface ActivityDownloader {
//    boolean login();

    List<Activity> getActivitiesList();

    geoJSON downloadActivity(Long id);

}
