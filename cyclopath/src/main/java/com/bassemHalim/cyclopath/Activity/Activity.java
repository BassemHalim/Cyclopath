
package com.bassemHalim.cyclopath.Activity;

import com.bassemHalim.cyclopath.User.User;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import com.bassemHalim.cyclopath.Utils.CompositeKeyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class Activity {
    //    @DynamoDbAttribute(value = "CyclopathPK")
    private String ownerUUID;
    private CompositeKey sortKey;
    //    @DynamoDbAttribute(attributeName = "SK")
    private final String SK = "ACTIVITY";
    //    @DynamoDbAttribute(value = "activityID")
    private long activityId;
    //    @DynamoDbAttribute(value = "name")
    private String activityName;
    //    @DynamoDbAttribute(attributeName = "avgHR")
    private double averageHR;
    //    @DynamoDbAttribute(attributeName = "avgSpeed")
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
    //    @DynamoDbAttribute(attributeName = "endLat")
    private double endLatitude;
    //    @DynamoDbAttribute(attributeName = "endLon")
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
    @DynamoDbAttribute(attributeName = "startLat")
    private double startLatitude;
    @DynamoDbAttribute(attributeName = "startLon")
    private double startLongitude;
    private String startTimeGMT;
    private String startTimeLocal;
    private int timeZoneId;
    //    public boolean userPro;
    private double waterEstimated;
    private byte[] geoJSON_gzip;

    @DynamoDBTypeConverted(converter = CompositeKeyConverter.class) // FIXME: 5/12/2023
    @DynamoDbSortKey
    @DynamoDbAttribute(value = "CyclopathSK")
    public CompositeKey getSortKey() {
        if (sortKey == null) {
            sortKey = new CompositeKey(SK, String.valueOf(activityId));
        }
        return sortKey;
    }

    @DynamoDBTypeConverted(converter = CompositeKeyConverter.class) // FIXME: 5/12/2023
    public void setSortKey(CompositeKey key) {
        sortKey = key;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "CyclopathPK")
    public String getOwnerUUID() {
        if (ownerUUID == null) {
            ownerUUID = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        }
        return ownerUUID;
    }

    @DynamoDbIgnore
    public String getSK() {
        return this.SK;
    }
}

