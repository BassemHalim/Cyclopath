package com.bassemHalim.cyclopath.Activity;

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
    ResponseEntity<List<ActivityDTO>> getActivityList() {
        List<ActivityDTO> activityList = activityService.getActivityList();
        return ResponseEntity.ok(activityList);
    }


    @GetMapping("/{id}")
    ResponseEntity<ActivityDTO> getActivity(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivity(id));
    }


}
