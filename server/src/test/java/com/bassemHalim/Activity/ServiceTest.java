//package com.bassemHalim.cyclopath.Activity;
//
//import ActivityDownloader.Activity.com.bassemHalim.ActivityDownloader;
//import Repositoy.com.bassemHalim.SingleTableDB;
//import User.com.bassemHalim.User;
//import User.com.bassemHalim.UserService;
//import Utils.com.bassemHalim.CompositeKey;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.security.core.context.SecurityContext;
//
//import java.nio.charset.StandardCharsets;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ServiceTest {
//    @Mock
//    SingleTableDB repository;
//    @Mock
//    ActivityDownloader garminDownloader;
//    @Mock
//    SecurityContext securityContext;
//    @InjectMocks
//    ActivityService service;
//
//    @Test
//    public void TestgetActivitiesMetatdata() {
//
//    }
//
//
//    @Test
//    public void TestgetActivityList() {
//
//    }
//
//    @Test
//    public void TestgetActivity() {
//        long activityId = 123L;
//        Activity activity = new Activity();
//        activity.setActivityId(activityId);
//        activity.setGeoJSON_gzip("test".getBytes(StandardCharsets.UTF_8));
//        activity.setActivityName("test");
//        when(repository.getActivity(anyString(), any(CompositeKey.class))).thenReturn(activity);
//
//        ActivitiesMetatdata metatdata = new ActivitiesMetatdata();
//        metatdata.addActivity(activityId);
//        when(repository.getActivityMetadata(anyString())).thenReturn(metatdata);
//        User johndoe = User.builder().Id("UUID").build();
//        try (MockedStatic<UserService> mockedStatic = Mockito.mockStatic(UserService.class)) {
//            mockedStatic.when(() -> UserService.getCurrentUser()).thenReturn(johndoe);
//        }
//        ActivityDTO activityDTO = service.getActivity(activityId);
//        assert (activityDTO != null);
//        assert (activityDTO.getActivityId() == activityId);
//        assert (activityDTO.getActivityName().equals("test"));
//
//
//    }
//
//    @Test
//    public void TestdeleteActivityList() {
//
//    }
//
//    @Test
//    public void TestgetActivityRoute() {
//
//    }
//
//
//}
