package com.bassemHalim.cyclopath.Map;


import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@Log
public class MapService {

    /**
     * returns an S3 URL pointing to the static map image of that activity
     *
     * @param activityID
     * @return URL to the image
     */
    public URL getMap(long activityID) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * given a route object, generate a static map and save it to s3
     *
     * @param route Route object with the geoJSON route
     * @return the private url where the route image is stored / maybe an object id to be stored in Route
     */
    public URL putMap(Route route) {
        throw new RuntimeException("unimplemented");

    }
}
