package com.bassemHalim.cyclopath.Activity;


import com.bassemHalim.cyclopath.Activity.ActivityDownloader.ActivityDownloader;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import com.bassemHalim.cyclopath.Repositoy.SingleTableDB;
import com.bassemHalim.cyclopath.User.User;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import com.bassemHalim.cyclopath.Utils.Compressor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private List<ActivityDTO> syncActivities(int start, int limit) {
        List<ActivityListItemDTO> garminActivityList = garminDownloader.getActivitiesList(start, limit);
        ActivitiesMetatdata savedActivities = getActivitiesMetatdata();

        if (savedActivities != null && savedActivities.getSavedActivities().size() == garminActivityList.size()) {
            // up to date
            return ActivityMapper.MAPPER.ActivityListItemDTOtoDTOList(garminActivityList);
        }

        List<Activity> downloaded = new ArrayList<>();
        List<Long> inRepo = savedActivities.getSavedActivities();
        // need to update the list of activities and download the new ones
        for (ActivityListItemDTO activity : garminActivityList) {
            Long ActivityID = activity.getActivityId();
            if (!inRepo.contains(ActivityID)) {
                Activity garminActivity = garminDownloader.getActivity(ActivityID);
                byte[] route = garminDownloader.getActivityRoute(ActivityID);
                garminActivity.setGeoJSON_gzip(route);
                downloaded.add(garminActivity);
                try {
                    Thread.sleep(600 + (long) (Math.random() * 100));
                } catch (InterruptedException ex) {
                    log.severe("thread sleep error" + ex.toString());
                }
            }
            savedActivities.addActivity(ActivityID);
        }
        // update the activity list in the DB

        repository.batchSaveActivities(downloaded);

        repository.saveActivitiesMetadata(savedActivities);
        return ActivityMapper.MAPPER.ActivityListItemDTOtoDTOList(garminActivityList);
    }

    private ActivitiesMetatdata getActivitiesMetatdata() {
        String UUID = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ActivitiesMetatdata savedActivities = repository.getActivityMetadata(UUID);
        if (savedActivities == null) {
            savedActivities = new ActivitiesMetatdata();
            savedActivities.setUUID(UUID);

        }
        return savedActivities;
    }

    public List<ActivityDTO> getActivityList(@NotNull @Positive int start, @NotNull @Positive int limit) {
        return syncActivities(start, limit);
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
        String UUID = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        if (activitiesMetatdata.getSavedActivities().contains(ID)) {
            // it's in the DB
            Activity activity = repository.getActivityById(UUID, new CompositeKey("ACTIVITY", ID.toString()));
            if (activity.getGeoJSON_gzip() == null) {
                activity.setGeoJSON_gzip(garminDownloader.getActivityRoute(ID));
                repository.saveActivity(activity);
            }
            byte[] decompressed = Compressor.decompress(activity.getGeoJSON_gzip());
            return ActivityMapper.MAPPER.toDTO(activity);
        }
        // need to update the list of activities and download the new ones
        syncActivities(0, MAX_USER_ACTIVITIES);
        // Activity should now be in DB
        activitiesMetatdata = getActivitiesMetatdata();
        if (!activitiesMetatdata.getSavedActivities().contains(ID)) {
            // if ID is still missing => return null
            return null;
        }
        Activity activity = repository.getActivityById(UUID, new CompositeKey("ACTIVITY", ID.toString()));
        return ActivityMapper.MAPPER.toDTO(activity);
    }

    public void putActivity(Activity activity) {
    }

    public void deleteActivity(@NotNull @Positive Long ID) {
        String UUID = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ActivitiesMetatdata activitiesMetatdata = getActivitiesMetatdata();
        activitiesMetatdata.removeActivity(ID);
        repository.saveActivitiesMetadata(activitiesMetatdata);
        repository.deleteActivity(UUID, new CompositeKey("ACTIVITY", ID.toString()));
    }
}
