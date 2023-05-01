package com.bassemHalim.cyclopath.Activity;

import com.bassemHalim.cyclopath.geoJSON.geoJSON;
import io.jenetics.jpx.GPX;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ActivityDownloader service;


    @GetMapping("/activity-list")
    List<Activity> getActivityList() {
        return service.getActivitiesList();
    }

    @GetMapping("/{id}")
    JSONObject getActivity(@PathVariable Long id) {
        return service.downloadActivity(id);
    }


}
