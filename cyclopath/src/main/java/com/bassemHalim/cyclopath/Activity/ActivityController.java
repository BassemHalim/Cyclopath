package com.bassemHalim.cyclopath.Activity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/activity")
@Getter
public class ActivityController {

    @Autowired
    private ActivityDownloader service;
    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping("/activity-list")
    List<Activity> getActivityList() {
        List<Activity> activityList = service.getActivitiesList();
        for (Activity activity : activityList) {
            byte[] geoJSON_gzip = service.downloadActivity(activity.getActivityId());
            activity.setGeoJSON_gzip(geoJSON_gzip);
            activityRepository.save(activity);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        return activityList;
    }

//    @GetMapping("/{id}")
//    JSONObject getActivity(@PathVariable Long id) {
//        return service.downloadActivity(id);
//    }


}
