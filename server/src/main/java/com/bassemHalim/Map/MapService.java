package com.bassemHalim.Map;


import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.bassemHalim.Activity.ActivityDownloader.GarminDownloader;
import com.bassemHalim.Activity.ActivityService;
import com.bassemHalim.Repositoy.MapRepository;
import com.bassemHalim.Repositoy.SingleTableDB;
import com.bassemHalim.User.UserService;
import com.bassemHalim.Utils.CompositeKey;
import com.bassemHalim.Utils.Compressor;
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
        // check if in S3
        String key = UserService.getCurrentUser().getId() + String.format("#%012d", activityID);
        if (mapRepository.objectExists(key)) {
            return Optional.of(mapRepository.generatePresignedUrl(key).orElseThrow());
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
                .width(300)
                .height(200)
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
            byte[] json = garminDownloader.getActivityRoute(ID); // gets blocked sometimes
            route = Route.builder()
                    .activityID(ID)
                    .geoJSON_zip(json)
                    .build();
            repository.saveRoute(route);


//            String json = garminDownloader.getActivityRoute2(ID);
//            Gson gson = new Gson();
//            JsonObject response = gson.fromJson(json, JsonElement.class).getAsJsonObject();
//            JsonObject geoJSONDTO = response.get("geoPolylineDTO").getAsJsonObject();
//            JsonArray polyline = geoJSONDTO.get("polyline").getAsJsonArray();
//            FeatureCollection collection = FeatureCollection.fromJson(polyline.getAsString());
//
//            System.out.println(collection);
        }
//        System.out.println(route);
        return route;
    }
}
