
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
    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", averageHR=" + averageHR +
                ", averageSpeed=" + averageSpeed +
                ", beginTimestamp=" + beginTimestamp +
                ", calories=" + calories +
                '}';
    }

    @DynamoDBHashKey(attributeName = "pk")
    private long activityId;
    @DynamoDBAttribute()
    private String activityName;
    public double averageHR;
    public double averageSpeed;
    public long beginTimestamp;
    public double bmrCalories;
    public double calories;
    public long deviceId;
    public double distance;
    public double duration;
    public double elapsedDuration;
    public boolean elevationCorrected;
    public double elevationGain;
    public double elevationLoss;
    public double endLatitude;
    public double endLongitude;
    public boolean favorite;
    public boolean hasPolyline;
    public boolean hasSplits;
    public boolean hasVideo;
    public int lapCount;
    public String locationName;
    public boolean manualActivity;
    public String manufacturer;
    public double maxElevation;
    public double maxHR;
    public double maxSpeed;
    public double maxVerticalSpeed;
    public double minActivityLapDuration;
    public double minElevation;
    public double movingDuration;
    public String ownerDisplayName;
    public String ownerFullName;
    public int ownerId;
    //    public String ownerProfileImageUrlLarge;
//    public String ownerProfileImageUrlMedium;
//    public String ownerProfileImageUrlSmall;
//    public boolean parent;
//    public boolean pr;
    //    public boolean purposeful;
    public int sportTypeId;
    public double startLatitude;
    public double startLongitude;
    public String startTimeGMT;
    public String startTimeLocal;
    public int timeZoneId;
    //    public boolean userPro;
    public double waterEstimated;

    public byte[] geoJSON_gzip;


}

