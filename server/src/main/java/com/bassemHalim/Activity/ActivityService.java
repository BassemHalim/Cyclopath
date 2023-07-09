package com.bassemHalim.Activity;


import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.bassemHalim.Activity.ActivityDownloader.ActivityDownloader;
import com.bassemHalim.Utils.CompositeKey;
import com.bassemHalim.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.Repositoy.SingleTableDB;
import com.bassemHalim.User.UserService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private List<ActivityDTO> syncActivities() {
        List<ActivityListItemDTO> garminActivityList =
                garminDownloader.getActivitiesList(
                        0,
                        MAX_USER_ACTIVITIES);
        ActivitiesMetatdata savedActivities = getActivitiesMetatdata();
        if (savedActivities != null && savedActivities.getSavedActivities()
                .size() == garminActivityList.size()) {
            // up to date
            return ActivityMapper.MAPPER.ActivityListItemDTOtoDTOList(
                    garminActivityList);
        }

        List<Activity> downloaded = new LinkedList<>();
        List<Long> inRepo = savedActivities.getSavedActivities();
        // need to update the list of activities and download the new ones
        for (ActivityListItemDTO activity : garminActivityList) {
            Long ActivityID = activity.getActivityId();
            if (!inRepo.contains(ActivityID)) {
                Activity garminActivity = garminDownloader.getActivity(
                        ActivityID);
                garminActivity.setWeather(garminDownloader.getActivityWeather(
                        ActivityID));
                downloaded.add(garminActivity);
                try {
                    // to not spam the garmin server
                    Thread.sleep(100 + (long) (Math.random() * 100));
                } catch (InterruptedException ex) {
                    log.severe("thread sleep error" + ex);
                }
                // will be big the first time
                savedActivities.addActivity(ActivityMapper.MAPPER.toDTO(activity));
            }
        }
        // update the activity list in the DB
        repository.batchSaveActivities(downloaded);
        repository.saveActivitiesMetadata(savedActivities);
        return ActivityMapper.MAPPER.ActivityListItemDTOtoDTOList(
                garminActivityList);
    }

    public ActivitiesMetatdata getActivitiesMetatdata() {
        String UUID = UserService.getCurrentUser().getId();
        ActivitiesMetatdata savedActivities = repository.getActivityMetadata(
                UUID);
        if (savedActivities == null) {
            savedActivities = new ActivitiesMetatdata();
            savedActivities.setUUID(UUID);
        }
        return savedActivities;
    }

    public List<Double> getDistanceHistory() {
        ActivitiesMetatdata metatdata = getActivitiesMetatdata();
        return metatdata.getYearlyHistory();
    }

    public List<ActivityDTO> getActivityList(@NotNull @PositiveOrZero long startActivityID,
                                             @NotNull @Positive int limit,
                                             boolean sync) {
        if (sync) {
            syncActivities();
        }
        List<Activity> activityList = repository.batchGetActivity(limit,
                                                                  UserService.getCurrentUser()
                                                                          .getId(), startActivityID);

        return ActivityMapper.MAPPER.ActivitytoDTOList(activityList);
    }

    /**
     * @param ID Activity ID
     * @return ActivityDTO or null if not found
     */
    public ActivityDTO getActivity(@NotNull @Positive Long ID) {

        // check if activity in user's activity list in Dynamo, if so get it and return it
        if (!activityExists(ID))
            throw new ResourceNotFoundException("activity not found");
        String UUID = UserService.getCurrentUser().getId();
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        if (activitiesMetatdata.getSavedActivities().contains(ID)) {
            // it's in the DB
            Activity activity = repository.getActivity(UUID,
                                                       new CompositeKey(
                                                               "ACTIVITY",
                                                               Activity.getCompositeKeyPostfix(
                                                                       ID)));
            return ActivityMapper.MAPPER.toDTO(activity);
        }
        // need to update the list of activities and download the new ones
        syncActivities();
        // Activity should now be in DB if not will throw error
        Activity activity = repository.getActivity(UUID,
                                                   new CompositeKey("ACTIVITY",
                                                                    ID.toString()));
        return ActivityMapper.MAPPER.toDTO(activity);
    }


    public void deleteActivity(@NotNull @Positive Long ID) {
        if (!activityExists(ID))
            throw new ResourceNotFoundException("activity not found");
        String UUID = UserService.getCurrentUser().getId();
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        activitiesMetatdata.removeActivity(ID);
        repository.saveActivitiesMetadata(activitiesMetatdata);
        repository.deleteActivity(UUID,
                                  new CompositeKey("ACTIVITY", ID.toString()));
    }

    public Boolean activityExists(@NotNull @Positive Long ID) {
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        if (activitiesMetatdata.hasActivity(ID)) {
            return true;
        }
        List<ActivityListItemDTO> activityListItemDTOS =
                garminDownloader.getActivitiesList(
                        0,
                        MAX_USER_ACTIVITIES); // @fixme change to fetch all
        return activityListItemDTOS.stream()
                .anyMatch(a -> a.getActivityId() == ID);
    }
}
