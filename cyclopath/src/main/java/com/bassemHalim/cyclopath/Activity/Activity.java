
package com.bassemHalim.cyclopath.Activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Activity")

public class Activity {
//    @Override
//    public String toString() {
//        return "Activity{" +
//                "activityId=" + activityId +
//                ", activityName='" + activityName + '\'' +
//                ", averageHR=" + averageHR +
//                ", averageSpeed=" + averageSpeed +
//                ", beginTimestamp=" + beginTimestamp +
//                ", calories=" + calories +
//                '}';
//    }

    @DynamoDBHashKey(attributeName = "pk")
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


}

