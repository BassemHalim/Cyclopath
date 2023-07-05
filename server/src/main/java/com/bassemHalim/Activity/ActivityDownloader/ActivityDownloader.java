package com.bassemHalim.Activity.ActivityDownloader;

import com.bassemHalim.Activity.Activity;
import com.bassemHalim.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.Weather.Weather;

import java.util.List;

public interface ActivityDownloader {
//    boolean login();

    List<ActivityListItemDTO> getActivitiesList(int start, int limit);

    Activity getActivity(Long ID);

    byte[] getActivityRoute(Long ID);

    Weather getActivityWeather(Long ID);

}
