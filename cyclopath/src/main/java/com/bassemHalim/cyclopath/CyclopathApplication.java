package com.bassemHalim.cyclopath;

import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.Activity.GarminDownloader;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

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
//                downloader.downloadActivity(activityList.get(2).getActivityId());
            } catch (Exception e) {
                System.out.println("couldn't save to file");
            }

        }
//        GPX gpx = downloader.downloadActivity(10823318529L);
//        List<Track> trackList = gpx.getTracks();
//        for (TrackSegment seg : trackList.get(0)) {
//            List<WayPoint> pts = seg.getPoints();
//            for (WayPoint pt : pts) {
//                System.out.println(pt);
//                Optional<Document> doc = pt.getExtensions();
//                if (doc.isPresent()) {
//                    System.out.println(doc.get().getElementsByTagName("ns3:hr").item(0).getFirstChild().getNodeValue());
//                }
//
//            }
//        }
    }

}

