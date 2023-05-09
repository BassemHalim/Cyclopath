package com.bassemHalim.cyclopath.Activity;

import com.bassemHalim.cyclopath.ActivityListItemDTO.ActivityListItemDTO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activity")
@Getter
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/activity-list")
    ResponseEntity<List<ActivityListItemDTO>> getActivityList() {
        List<ActivityListItemDTO> activityList = activityService.getActivityList();
//        for (Activity activity : activityList) {
//            byte[] geoJSON_gzip = service.downloadActivityRoute(activity.getActivityId());
//            activity.setGeoJSON_gzip(geoJSON_gzip);
//            try {
//                TimeUnit.MILLISECONDS.sleep((long) Math.random() * 100);
//            } catch (InterruptedException ie) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        activityRepository.batchSave(activityList);
        return ResponseEntity.ok(activityList);
    }


    @GetMapping("/{id}")
    Activity getActivity(@PathVariable Long id) {
        return activityService.getActiviy(id);
    }


}
