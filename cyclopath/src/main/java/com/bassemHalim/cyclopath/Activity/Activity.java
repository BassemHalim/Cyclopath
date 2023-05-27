
package com.bassemHalim.cyclopath.Activity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.bassemHalim.cyclopath.User.User;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import com.bassemHalim.cyclopath.Utils.CompositeKeyConverter;
import com.bassemHalim.cyclopath.Weather.Weather;
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
    //    private boolean favorite;
    private boolean hasPolyline;
    private boolean hasSplits;
    //    private boolean hasVideo;
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
    //    private String ownerDisplayName;
//    private String ownerFullName;
    private int ownerId;
    //    public String ownerProfileImageUrlLarge;
//    public String ownerProfileImageUrlMedium;
//    public String ownerProfileImageUrlSmall;
//    public boolean parent;
//    public boolean pr;
//    public boolean purposeful;
//    private int sportTypeId;
    @DynamoDBAttribute(attributeName = "startLat")
    private double startLatitude;
    @DynamoDBAttribute(attributeName = "startLon")
    private double startLongitude;
    private String startTimeGMT;
    private String startTimeLocal;
    private int timeZoneId;
    //    public boolean userPro;
    private double waterEstimated;
    private byte[] geoJSON_gzip;
    private Weather weather;

    @DynamoDBTypeConverted(converter = CompositeKeyConverter.class) // FIXME: 5/12/2023
    @DynamoDBRangeKey(attributeName = "CyclopathSK")
    public CompositeKey getSortKey() {
        if (sortKey == null || sortKey.getPostfix().startsWith("8") || sortKey.getPostfix().startsWith("9")) { // @FIXME remove

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
            ownerUUID = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
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
        return "0" .repeat(numZeros) + activityID;
    }
}

