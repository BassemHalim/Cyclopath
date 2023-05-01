
package com.bassemHalim.cyclopath.Activity;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

    private Long activityId;
    private String activityName;
    private ActivityType activityType;
    private Boolean atpActivity;
    private Boolean autoCalcCalories;
    private Object averageBikingCadenceInRevPerMinute;
    private Double averageHR;
    private Double averageSpeed;
    private Object avgPower;
    private Long beginTimestamp;
    private Double bmrCalories;
    private Double calories;
    private Object caloriesConsumed;
    private Object caloriesEstimated;
    private Object comments;
    private Object courseId;
    private Boolean decoDive;
    private Long deviceId;
    private Double distance;
    private Double duration;
    private Double elapsedDuration;
    private Boolean elevationCorrected;
    private Double elevationGain;
    private Double elevationLoss;
    private Double endLatitude;
    private Double endLongitude;
    private Boolean favorite;
    private Boolean hasPolyline;
    private Boolean hasSplits;
    private Boolean hasVideo;
    private Long lapCount;
    private String locationName;
    private Boolean manualActivity;
    private String manufacturer;
    private Object maxBikingCadenceInRevPerMinute;
    private Double maxElevation;
    private Double maxHR;
    private Object maxPower;
    private Double maxSpeed;
    private Object maxTemperature;
    private Double maxVerticalSpeed;
    private Double minActivityLapDuration;
    private Double minElevation;
    private Object minTemperature;
    private Double movingDuration;
    private String ownerDisplayName;
    private String ownerFullName;
    private Long ownerId;
    private String ownerProfileImageUrlLarge;
    private String ownerProfileImageUrlMedium;
    private String ownerProfileImageUrlSmall;
    private Boolean parent;
    private Object parentId;
    private Boolean pr;
    private Boolean purposeful;
    private List<Object> splitSummaries;
    private Long sportTypeId;
    private Double startLatitude;
    private Double startLongitude;
    private String startTimeGMT;
    private String startTimeLocal;
    private Long timeZoneId;
    private Boolean userPro;
    private Object videoUrl;
    private Object waterConsumed;
    private Double waterEstimated;

}

