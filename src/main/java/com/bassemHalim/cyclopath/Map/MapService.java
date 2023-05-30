package com.bassemHalim.cyclopath.Map;


import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminDownloader;
import com.bassemHalim.cyclopath.Activity.ActivityService;
import com.bassemHalim.cyclopath.Repositoy.MapRepository;
import com.bassemHalim.cyclopath.Repositoy.SingleTableDB;
import com.bassemHalim.cyclopath.User.UserService;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import com.bassemHalim.cyclopath.Utils.Compressor;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.geojson.*;
import com.mapbox.turf.TurfTransformation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.java.Log;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class MapService {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private SingleTableDB repository;
    @Autowired
    private GarminDownloader garminDownloader;
    @Autowired
    private MapRepository mapRepository;
    @Value("${mapbox.publicKey}")
    private String MAPBOX_ACCESS_TOKEN;

    /**
     * returns an S3 URL pointing to the static map image of that activity
     *
     * @param activityID
     * @return URL to the image
     */
    public Optional<HttpUrl> getMap(long activityID) {
        //@TODO change logic to get image from s3
        log.info("getting map");
        // check if in S3
        String key = UserService.getCurrentUser().getId() + String.format("#%012d", activityID);
        if (mapRepository.objectExists(key)) {
            return Optional.of(mapRepository.generatePresignedUrl(key).orElseThrow());
//            byte[] data = mapRepository.getObject(key).orElseThrow();
//            ByteArrayInputStream bis = new ByteArrayInputStream(data);
//            try {
//                BufferedImage bImage2 = ImageIO.read(bis);
//                ImageIO.write(bImage2, "png", new File("/mnt/projects/BikeApp/cyclopath/activities_samples/output.png"));
//
//            } catch (Exception e) {
//                log.info(e.getMessage());
//            }
        }
        // if not in S3 get route
        Route route = getRoute(activityID);
        HttpUrl url = generateMap(route).orElseThrow();
        return Optional.of(url);

    }

    /**
     * given a route object, generate a static map and save it to s3
     *
     * @param route Route object with the geoJSON route
     * @return the private url where the route image is stored / maybe an object id to be stored in Route
     */
    public Optional<HttpUrl> generateMap(Route route) {
        log.info("generating map for " + route.getActivityID());
        String json = new String(Compressor.decompress(route.getGeoJSON_zip()));
        FeatureCollection collection = FeatureCollection.fromJson(json);
        Feature feature = collection.features().get(0);
        Geometry geometry = feature.geometry();
        if (!(geometry instanceof LineString)) {
            return Optional.empty();
        }
        LineString line = (LineString) geometry;
//        double tolerance = 0.00001; // 1m of error
        double tolerance = 0.001; // 100m tolerance
        List<Point> simplified = TurfTransformation.simplify(line.coordinates(), tolerance);
        LineString simplifiedline = LineString.fromLngLats(simplified);
        FeatureCollection newCollection = FeatureCollection.fromFeature(Feature.fromGeometry(simplifiedline));


        MapboxStaticMap staticImage = MapboxStaticMap.builder()
                .accessToken(MAPBOX_ACCESS_TOKEN)
                .styleId(StaticMapCriteria.OUTDOORS_STYLE)
                .cameraAuto(true)
                .geoJson(newCollection)
                .width(300) // Image width
                .height(200) // Image height
                .retina(true) // Retina 2x image will be returned
                .logo(false)
                .build();
        String key = UserService.getCurrentUser().getId() + String.format("#%012d", route.getActivityID());
        try {
            BufferedImage map = ImageIO.read(staticImage.url().url());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(map, "png", bos);
            byte[] data = bos.toByteArray();
            mapRepository.putObject(key, data);

//            ImageIO.write(image, "png", new File("/mnt/projects/BikeApp/cyclopath/activities_samples/image.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(mapRepository.generatePresignedUrl(key).orElseThrow());
    }

    public Route getRoute(@NotNull @Positive Long ID) {
        String UUID = UserService.getCurrentUser().getId();
        if (!activityService.activityExists(ID)) throw new ResourceNotFoundException("didn't find activity");
        Route route = repository.getRoute(UUID, new CompositeKey("ROUTE", Long.toString(ID)));
        if (route == null) {
            byte[] json = garminDownloader.getActivityRoute(ID);
            route = Route.builder()
                    .activityID(ID)
                    .geoJSON_zip(json)
                    .build();
            repository.saveRoute(route);
        }
        return route;
    }
}