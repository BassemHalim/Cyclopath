package com.bassemHalim.cyclopath;

import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader;
import com.bassemHalim.cyclopath.Activity.GarminDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class CyclopathApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CyclopathApplication.class, args);
        GarminDownloader downloader = context.getBean(GarminDownloader.class);
        if (downloader.login()) {
            try {
                List<Activity> activityList = downloader.getActivitiesList();
//                for (Activity activity : activityList) {
//                    System.out.println(activity);
//                }
                downloader.downloadActivity(activityList.get(2));
            } catch (IOException e) {
                System.out.println("couldn't save to file");
            }

        }
    }
}
