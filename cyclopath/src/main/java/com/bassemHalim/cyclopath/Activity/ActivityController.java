package com.bassemHalim.cyclopath.Activity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
@Getter
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/activity-list")
    ResponseEntity<List<ActivityDTO>> getActivityList(
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false, defaultValue = "0") int start) {
        List<ActivityDTO> activityList = activityService.getActivityList(start, limit);
        return ResponseEntity.ok(activityList);
    }


    @GetMapping("/{id}")
    ResponseEntity<ActivityDTO> getActivity(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivity(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok("activity deleted");
    }


//    @GetMapping("migrate")
//    ResponseEntity<String> migrate() {
//        activityService.updateSortKeys();
//        return ResponseEntity.ok("done");
//    }
}
