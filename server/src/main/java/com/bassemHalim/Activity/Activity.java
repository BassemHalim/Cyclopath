
package com.bassemHalim.Activity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.bassemHalim.Utils.CompositeKey;
import com.bassemHalim.Utils.CompositeKeyConverter;
import com.bassemHalim.User.User;
import com.bassemHalim.Weather.Weather;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Cyclopath")

public class Activity {
    @DynamoDBAttribute(attributeName = "CyclopathPK")
    private String ownerUUID;
    private CompositeKey sortKey;
    @DynamoDBIgnore
    @DynamoDBAttribute(attributeName = "SK")
    private final String SK = "ACTIVITY";
    @DynamoDBAttribute(attributeName = "activityID")
    private long activityId;
    @DynamoDBAttribute(attributeName = "name")
    private String activityName;
    @DynamoDBAttribute(attributeName = "avgHR")
    private double averageHR;
    @DynamoDBAttribute(attributeName = "avgSpeed")
    private double averageSpeed;
    //    @DynamoDBAttribute(attributeName = "start")
    //    private long beginTimestamp;
    private double bmrCalories;
    private double calories;
    //    private long deviceId;
    private double distance;
    private double duration;
    private double elapsedDuration;
    private boolean elevationCorrected;
    private double elevationGain;
    private double elevationLoss;
    @DynamoDBAttribute(attributeName = "endLat")
    private double endLatitude;
    @DynamoDBAttribute(attributeName = "endLon")
    private double endLongitude;
    private boolean hasPolyline;
    private boolean hasSplits;
    private int lapCount;
    private String locationName;
    private boolean manualActivity;
    private String manufacturer;
    private double maxElevation;
    private double maxHR;
    private double maxSpeed;
    private double maxVerticalSpeed;
    private double minActivityLapDuration;
    private double minElevation;
    private double movingDuration;
    private int ownerId;
    @DynamoDBAttribute(attributeName = "startLat")
    private double startLatitude;
    @DynamoDBAttribute(attributeName = "startLon")
    private double startLongitude;
    private String startTimeGMT;
    private String startTimeLocal;
    private int timeZoneId;
    private double waterEstimated;
    private byte[] geoJSON_gzip;
    private Weather weather;

    @DynamoDBTypeConverted(converter = CompositeKeyConverter.class)
    @DynamoDBRangeKey(attributeName = "CyclopathSK")
    public CompositeKey getSortKey() {
        if (sortKey == null) {
            sortKey = new CompositeKey(SK, getCompositeKeyPostfix(activityId));
        }
        return sortKey;
    }

    @DynamoDBTypeConverted(converter = CompositeKeyConverter.class)
    public void setSortKey(CompositeKey key) {
        sortKey = key;
    }

    @DynamoDBHashKey(attributeName = "CyclopathPK")
    public String getOwnerUUID() {
        if (ownerUUID == null) {
            ownerUUID = ((User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal()).getId();
        }
        return ownerUUID;
    }

    @DynamoDBIgnore
    public String getSK() {
        return this.SK;
    }

    public static String getCompositeKeyPostfix(long id) {
        String activityID = String.valueOf(id);
        int numZeros = 11 - activityID.length();
        return "0".repeat(numZeros) + activityID;
    }
}

