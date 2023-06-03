package com.bassemHalim.cyclopath.Activity;


import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.ActivityDownloader;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.cyclopath.Map.Route;
import com.bassemHalim.cyclopath.Repositoy.SingleTableDB;
import com.bassemHalim.cyclopath.User.UserService;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
@Log
public class ActivityService {
    @Autowired
    private ActivityDownloader garminDownloader;
    @Autowired
    private SingleTableDB repository;
    private final int MAX_USER_ACTIVITIES = 1000;

    /**
     * Download activities from garmin and saveUser them to dynamo
     * returns list of activities metadata (w/o route)
     */
    public List<ActivityDTO> syncActivities(int start, int limit) {
        List<ActivityListItemDTO> garminActivityList = garminDownloader.getActivitiesList(start, limit);
        ActivitiesMetatdata savedActivities = getActivitiesMetatdata();
        if (savedActivities != null && savedActivities.getSavedActivities().size() == garminActivityList.size()) {
            // up to date
            return ActivityMapper.MAPPER.ActivityListItemDTOtoDTOList(garminActivityList);
        }

        List<Activity> downloaded = new LinkedList<>();
        List<Long> inRepo = savedActivities.getSavedActivities();
        // need to update the list of activities and download the new ones
        for (ActivityListItemDTO activity : garminActivityList) {
            Long ActivityID = activity.getActivityId();
            if (!inRepo.contains(ActivityID)) {
                Activity garminActivity = garminDownloader.getActivity(ActivityID);
                garminActivity.setWeather(garminDownloader.getActivityWeather(ActivityID));
                downloaded.add(garminActivity);
                try {
                    // to not spam the garmin server
                    Thread.sleep(100 + (long) (Math.random() * 100));
                } catch (InterruptedException ex) {
                    log.severe("thread sleep error" + ex.toString());
                }
            }
            savedActivities.addActivity(ActivityID); // @fixme will be too big the first time
        }
        // update the activity list in the DB
        repository.batchSaveActivities(downloaded);
        repository.saveActivitiesMetadata(savedActivities);
        return ActivityMapper.MAPPER.ActivityListItemDTOtoDTOList(garminActivityList);
    }

    public ActivitiesMetatdata getActivitiesMetatdata() {
        String UUID = UserService.getCurrentUser().getId();
        ActivitiesMetatdata savedActivities = repository.getActivityMetadata(UUID);
        if (savedActivities == null) {
            savedActivities = new ActivitiesMetatdata();
            savedActivities.setUUID(UUID);
        }
        return savedActivities;
    }

    public List<ActivityDTO> getActivityList(@NotNull @Positive int start, @NotNull @Positive int limit) {

        List<Activity> activityList = repository.batchGetActivity(limit, UserService.getCurrentUser().getId());
        List<Activity> toBeSaved = new ArrayList<>(limit);
        List<Route> routesToBeSave = new LinkedList<>();
        for (Activity activity : activityList) {
            long activityID = activity.getActivityId();
            boolean updated = false;
            if (activity.getWeather() == null) {
                activity.setWeather(garminDownloader.getActivityWeather(activityID));
                updated = true;
            }
            if (activity.getGeoJSON_gzip() != null) {
                Route route = Route.builder()
                        .geoJSON_zip(activity.getGeoJSON_gzip())
                        .activityID(activity.getActivityId())
                        .build();
                route.setGeoJSON_zip(activity.getGeoJSON_gzip());
                routesToBeSave.add(route);
                activity.setGeoJSON_gzip(null);
                updated = true;
            }

            if (updated) {
                toBeSaved.add(activity);
            }
        }
        repository.batchSaveActivities(toBeSaved);
        repository.batchSaveRoute(routesToBeSave);
        return ActivityMapper.MAPPER.ActivitytoDTOList(activityList);
    }

    /**
     * @param ID
     * @return ActivityDTO or null if not found
     */
    public ActivityDTO getActivity(@NotNull @Positive Long ID) {
        // check if activity in user's activity list in Dynamo, if so get it and return it
        // if not get updated activity list from garmin
        // download the activity from garmin and saveUser it to dynamo
        // get activity list from dynamo
        if (!activityExists(ID)) throw new ResourceNotFoundException("activity not found");
        String UUID = UserService.getCurrentUser().getId();
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        if (activitiesMetatdata.getSavedActivities().contains(ID)) {
            // it's in the DB
            Activity activity = repository.getActivity(UUID, new CompositeKey("ACTIVITY", Activity.getCompositeKeyPostfix(ID)));
            if (activity.getGeoJSON_gzip() != null) {
                // @TODO change later after migration to new table scheme
                Route route = Route.builder()
                        .geoJSON_zip(activity.getGeoJSON_gzip())
                        .activityID(ID)
                        .build();
                route.setGeoJSON_zip(activity.getGeoJSON_gzip());
                repository.saveRoute(route);
                activity.setGeoJSON_gzip(null);
                activity.setWeather(garminDownloader.getActivityWeather(ID));
                repository.saveActivity(activity);
            }
            return ActivityMapper.MAPPER.toDTO(activity);
        }

        // need to update the list of activities and download the new ones
        syncActivities(0, MAX_USER_ACTIVITIES);
        // Activity should now be in DB if not throw error
        Activity activity = repository.getActivity(UUID, new CompositeKey("ACTIVITY", ID.toString()));
        return ActivityMapper.MAPPER.toDTO(activity);
    }


    public void deleteActivity(@NotNull @Positive Long ID) {
        if (!activityExists(ID)) throw new ResourceNotFoundException("activity not found");
        String UUID = UserService.getCurrentUser().getId();
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        activitiesMetatdata.removeActivity(ID);
        repository.saveActivitiesMetadata(activitiesMetatdata);
        repository.deleteActivity(UUID, new CompositeKey("ACTIVITY", ID.toString()));
    }

    public Boolean activityExists(@NotNull @Positive Long ID) {
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        if (activitiesMetatdata.hasActivity(ID)) {
            return true;
        }
        List<ActivityListItemDTO> activityListItemDTOS = garminDownloader.getActivitiesList(0, MAX_USER_ACTIVITIES); // @fixme change to fetch all
        return activityListItemDTOS.stream().anyMatch(a -> a.getActivityId() == ID);
    }
}
