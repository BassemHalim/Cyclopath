package com.bassemHalim.cyclopath.Activity.ActivityDownloader;

import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.cyclopath.Weather.Weather;

import java.util.List;

public interface ActivityDownloader {
//    boolean login();

    List<ActivityListItemDTO> getActivitiesList(int start, int limit);

    Activity getActivity(Long ID);

    byte[] getActivityRoute(Long ID);

    Weather getActivityWeather(Long ID);

}