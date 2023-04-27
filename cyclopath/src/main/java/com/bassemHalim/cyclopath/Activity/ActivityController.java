//package com.bassemHalim.cyclopath.Activity;
//
//import lombok.Getter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/activity")
//@Getter
//public class ActivityController {
//
//    @Autowired
//    private ActivityDownloader service;
//
//
//    @GetMapping("/activity-list")
//    List<Activity> getActivityList() {
//        service.login();
//        return service.getActivitiesList();
//    }
//}
