package com.bassemHalim.cyclopath.Activity;


import com.bassemHalim.cyclopath.ActivityDownloader.ActivityDownloader;
import com.bassemHalim.cyclopath.ActivityListItemDTO.ActivityListItemDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityDownloader downloader;
    @Autowired
    private ActivityRepository activityRepository;

    public List<ActivityListItemDTO> getActivityList() {
        return downloader.getActivitiesList();
    }

    public Activity getActiviy(@NotNull @Positive Long ID) {
        Activity activity = downloader.getActivity(ID);
        System.out.println(activity);
//        // check if it's in the DB
//        Activity activity = activityRepository.getActivityById(ID);
//        //check if found
//        if (activity == null) {
//            // wasn't in DB
//            activity = null; //TODO download activity from garmin
//        }
        return activity;
    }
}
