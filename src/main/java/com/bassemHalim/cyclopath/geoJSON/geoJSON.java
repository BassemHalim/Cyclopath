package com.bassemHalim.cyclopath.geoJSON;


import com.bassemHalim.cyclopath.Map.Track;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;

@Getter
@Log
public class geoJSON extends JSONObject {
    private final String type = "FeatureCollection";
    private ArrayList<Feature> features;

    public geoJSON() {
        this.features = new ArrayList<>();
    }

    /**
     * convert a string in gpx format to a json object
     *
     * @param gpx
     * @return
     */
    public geoJSON fromGPX(String gpx) {
        JSONObject json = XML.toJSONObject(gpx).getJSONObject("gpx");
        String time = json.getJSONObject("metadata").getString("time");
        JSONObject jsonTrack = json.getJSONObject("trk");
        String name = jsonTrack.getString("name");
        String type = jsonTrack.getString("type");
        Track track = new Track(name, Track.TrackType.valueOf(type), time);

        if (jsonTrack.has("trkseg")) {
            JSONObject trackSeg = jsonTrack.getJSONObject("trkseg");
            JSONArray points = trackSeg.getJSONArray("trkpt");
            for (int i = 0; i < points.length(); i++) {
                JSONObject pt = (JSONObject) points.get(i);
                double lat = pt.getDouble("lat");
                double lon = pt.getDouble("lon");
                double ele = pt.getDouble("ele");
                String timestamp = pt.getString("time");
                Track.Coordinate coordinate = new Track.Coordinate(lat, lon, ele);
                int hr = 0;
                try {// FIXME: 5/13/2023
                    hr = pt.getJSONObject("extensions").
                            getJSONObject("ns3:TrackPointExtension").getInt("ns3:hr");
                } catch (JSONException e) {
                    log.warning(e.toString());
                }
                track.appendData(timestamp, hr, coordinate);
            }
        }
        this.features.add(track);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        return this;


    }
}