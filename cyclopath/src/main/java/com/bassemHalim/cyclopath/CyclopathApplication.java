package com.bassemHalim.cyclopath;

import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader;
import com.bassemHalim.cyclopath.Activity.GarminDownloader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class CyclopathApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyclopathApplication.class, args);
        ActivityDownloader downloader = new GarminDownloader("", "");
        if (downloader.login()) {
            try {
                List<Activity> activityList = downloader.getActivitiesList();
//                for (Activity activity : activityList) {
//                    System.out.println(activity);
//                }
                downloader.downloadActivity(activityList.get(0));
            } catch (IOException e) {
                System.out.println("couldn't save to file");
            }
            System.out.println("done downloading?");

        }
    }
}
