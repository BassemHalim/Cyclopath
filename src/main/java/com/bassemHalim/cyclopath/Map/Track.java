package com.bassemHalim.cyclopath.Map;

import com.bassemHalim.cyclopath.geoJSON.Feature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonSerialize(using = Track.TrackSerializer.class)
public class Track implements Feature {
    public enum TrackType {
        walking,
        cycling,
        incident_detected
    }

    @Getter
    @AllArgsConstructor
    public static class Coordinate {
        @NotNull
        private double latitude;
        @NotNull
        private double longitude;
        @NotNull
        private double elevation;
    }


    private final String name;
    private final TrackType type;
    private final String time;


    private List<String> times;
    private List<Integer> heartRates;
    private List<Coordinate> coordinates;
    private final String _gpxType = "trk";


    public Track(String name, TrackType type, String time) {
        this.name = name;
        this.type = type;
        this.time = time;
        this.times = new ArrayList<>();
        this.heartRates = new ArrayList<>();
        this.coordinates = new ArrayList<>();
    }

    public void appendData(@NotNull @DateTimeFormat String time, @Positive Integer hr,
                           @NotNull @NotEmpty @Valid Coordinate coordinate) {

        this.times.add(time);
        this.heartRates.add(hr);
        this.coordinates.add(coordinate);
    }

    public static class TrackSerializer extends StdSerializer<Track> {
        public TrackSerializer() {
            this(null);
        }

        public TrackSerializer(Class<Track> t) {
            super(t);
        }

        @Override
        public void serialize(Track track, JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {

            gen.writeStartObject();
            gen.writeStringField("type", "Feature");

            gen.writeFieldName("properties");
            gen.writeStartObject();
            gen.writeStringField("_gpxType", track.get_gpxType());
            gen.writeStringField("name", track.getName());
            gen.writeStringField("type", track.getType().name());
            gen.writeStringField("time", track.getTime());

            gen.writeFieldName("coordinateProperties");
            gen.writeStartObject();

            gen.writeArrayFieldStart("times");
            for (String time : track.getTimes()) {
                gen.writeString(time);
            }
            gen.writeEndArray();

            gen.writeArrayFieldStart("ns3:TrackPointExtensions");
            for (Integer heartRate : track.getHeartRates()) {
                gen.writeNumber(heartRate);
            }
            gen.writeEndArray();
            gen.writeEndObject();
            gen.writeEndObject();
            gen.writeFieldName("geometry");
            gen.writeStartObject();
            gen.writeStringField("type", "LineString");
            gen.writeArrayFieldStart("coordinates");
            for (Track.Coordinate coordinate : track.getCoordinates()) {
                gen.writeStartArray();
                gen.writeNumber(coordinate.getLongitude());
                gen.writeNumber(coordinate.getLatitude());
                gen.writeNumber(coordinate.getElevation());
                gen.writeEndArray();
            }
            gen.writeEndArray();
            gen.writeEndObject();
            gen.writeEndObject();
        }
    }


}
