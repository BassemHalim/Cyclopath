
package com.bassemHalim.cyclopath.Activity;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Generated;

import java.util.List;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
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

    @Expose
    private Long activityId;
    @Expose
    private String activityName;
    @Expose
    private ActivityType activityType;
    @Expose
    private Boolean atpActivity;
    @Expose
    private Boolean autoCalcCalories;
    @Expose
    private Object averageBikingCadenceInRevPerMinute;
    @Expose
    private Double averageHR;
    @Expose
    private Double averageSpeed;
    @Expose
    private Object avgPower;
    @Expose
    private Long beginTimestamp;
    @Expose
    private Double bmrCalories;
    @Expose
    private Double calories;
    @Expose
    private Object caloriesConsumed;
    @Expose
    private Object caloriesEstimated;
    @Expose
    private Object comments;
    @Expose
    private Object courseId;
    @Expose
    private Boolean decoDive;
    @Expose
    private Long deviceId;
    @Expose
    private Double distance;
    @Expose
    private Double duration;
    @Expose
    private Double elapsedDuration;
    @Expose
    private Boolean elevationCorrected;
    @Expose
    private Double elevationGain;
    @Expose
    private Double elevationLoss;
    @Expose
    private Double endLatitude;
    @Expose
    private Double endLongitude;
    @Expose
    private Boolean favorite;
    @Expose
    private Boolean hasPolyline;
    @Expose
    private Boolean hasSplits;
    @Expose
    private Boolean hasVideo;
    @Expose
    private Long lapCount;
    @Expose
    private String locationName;
    @Expose
    private Boolean manualActivity;
    @Expose
    private String manufacturer;
    @Expose
    private Object maxBikingCadenceInRevPerMinute;
    @Expose
    private Double maxElevation;
    @Expose
    private Double maxHR;
    @Expose
    private Object maxPower;
    @Expose
    private Double maxSpeed;
    @Expose
    private Object maxTemperature;
    @Expose
    private Double maxVerticalSpeed;
    @Expose
    private Double minActivityLapDuration;
    @Expose
    private Double minElevation;
    @Expose
    private Object minTemperature;
    @Expose
    private Double movingDuration;
    @Expose
    private String ownerDisplayName;
    @Expose
    private String ownerFullName;
    @Expose
    private Long ownerId;
    @Expose
    private String ownerProfileImageUrlLarge;
    @Expose
    private String ownerProfileImageUrlMedium;
    @Expose
    private String ownerProfileImageUrlSmall;
    @Expose
    private Boolean parent;
    @Expose
    private Object parentId;
    @Expose
    private Boolean pr;
    @Expose
    private Boolean purposeful;
    @Expose
    private List<Object> splitSummaries;
    @Expose
    private Long sportTypeId;
    @Expose
    private Double startLatitude;
    @Expose
    private Double startLongitude;
    @Expose
    private String startTimeGMT;
    @Expose
    private String startTimeLocal;
    @Expose
    private Long timeZoneId;
    @Expose
    private Boolean userPro;
    @Expose
    private Object videoUrl;
    @Expose
    private Object waterConsumed;
    @Expose
    private Double waterEstimated;

    public Long getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Boolean getAtpActivity() {
        return atpActivity;
    }

    public Boolean getAutoCalcCalories() {
        return autoCalcCalories;
    }

    public Object getAverageBikingCadenceInRevPerMinute() {
        return averageBikingCadenceInRevPerMinute;
    }

    public Double getAverageHR() {
        return averageHR;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public Object getAvgPower() {
        return avgPower;
    }

    public Long getBeginTimestamp() {
        return beginTimestamp;
    }

    public Double getBmrCalories() {
        return bmrCalories;
    }

    public Double getCalories() {
        return calories;
    }

    public Object getCaloriesConsumed() {
        return caloriesConsumed;
    }

    public Object getCaloriesEstimated() {
        return caloriesEstimated;
    }

    public Object getComments() {
        return comments;
    }

    public Object getCourseId() {
        return courseId;
    }

    public Boolean getDecoDive() {
        return decoDive;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getDuration() {
        return duration;
    }

    public Double getElapsedDuration() {
        return elapsedDuration;
    }

    public Boolean getElevationCorrected() {
        return elevationCorrected;
    }

    public Double getElevationGain() {
        return elevationGain;
    }

    public Double getElevationLoss() {
        return elevationLoss;
    }

    public Double getEndLatitude() {
        return endLatitude;
    }

    public Double getEndLongitude() {
        return endLongitude;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public Boolean getHasPolyline() {
        return hasPolyline;
    }

    public Boolean getHasSplits() {
        return hasSplits;
    }

    public Boolean getHasVideo() {
        return hasVideo;
    }

    public Long getLapCount() {
        return lapCount;
    }

    public String getLocationName() {
        return locationName;
    }

    public Boolean getManualActivity() {
        return manualActivity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Object getMaxBikingCadenceInRevPerMinute() {
        return maxBikingCadenceInRevPerMinute;
    }

    public Double getMaxElevation() {
        return maxElevation;
    }

    public Double getMaxHR() {
        return maxHR;
    }

    public Object getMaxPower() {
        return maxPower;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public Object getMaxTemperature() {
        return maxTemperature;
    }

    public Double getMaxVerticalSpeed() {
        return maxVerticalSpeed;
    }

    public Double getMinActivityLapDuration() {
        return minActivityLapDuration;
    }

    public Double getMinElevation() {
        return minElevation;
    }

    public Object getMinTemperature() {
        return minTemperature;
    }

    public Double getMovingDuration() {
        return movingDuration;
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerProfileImageUrlLarge() {
        return ownerProfileImageUrlLarge;
    }

    public String getOwnerProfileImageUrlMedium() {
        return ownerProfileImageUrlMedium;
    }

    public String getOwnerProfileImageUrlSmall() {
        return ownerProfileImageUrlSmall;
    }

    public Boolean getParent() {
        return parent;
    }

    public Object getParentId() {
        return parentId;
    }

    public Boolean getPr() {
        return pr;
    }

    public Boolean getPurposeful() {
        return purposeful;
    }

    public List<Object> getSplitSummaries() {
        return splitSummaries;
    }

    public Long getSportTypeId() {
        return sportTypeId;
    }

    public Double getStartLatitude() {
        return startLatitude;
    }

    public Double getStartLongitude() {
        return startLongitude;
    }

    public String getStartTimeGMT() {
        return startTimeGMT;
    }

    public String getStartTimeLocal() {
        return startTimeLocal;
    }

    public Long getTimeZoneId() {
        return timeZoneId;
    }

    public Boolean getUserPro() {
        return userPro;
    }

    public Object getVideoUrl() {
        return videoUrl;
    }

    public Object getWaterConsumed() {
        return waterConsumed;
    }

    public Double getWaterEstimated() {
        return waterEstimated;
    }

    public static class Builder {

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

        public Activity.Builder withActivityId(Long activityId) {
            this.activityId = activityId;
            return this;
        }

        public Activity.Builder withActivityName(String activityName) {
            this.activityName = activityName;
            return this;
        }

        public Activity.Builder withActivityType(ActivityType activityType) {
            this.activityType = activityType;
            return this;
        }

        public Activity.Builder withAtpActivity(Boolean atpActivity) {
            this.atpActivity = atpActivity;
            return this;
        }

        public Activity.Builder withAutoCalcCalories(Boolean autoCalcCalories) {
            this.autoCalcCalories = autoCalcCalories;
            return this;
        }

        public Activity.Builder withAverageBikingCadenceInRevPerMinute(Object averageBikingCadenceInRevPerMinute) {
            this.averageBikingCadenceInRevPerMinute = averageBikingCadenceInRevPerMinute;
            return this;
        }

        public Activity.Builder withAverageHR(Double averageHR) {
            this.averageHR = averageHR;
            return this;
        }

        public Activity.Builder withAverageSpeed(Double averageSpeed) {
            this.averageSpeed = averageSpeed;
            return this;
        }

        public Activity.Builder withAvgPower(Object avgPower) {
            this.avgPower = avgPower;
            return this;
        }

        public Activity.Builder withBeginTimestamp(Long beginTimestamp) {
            this.beginTimestamp = beginTimestamp;
            return this;
        }

        public Activity.Builder withBmrCalories(Double bmrCalories) {
            this.bmrCalories = bmrCalories;
            return this;
        }

        public Activity.Builder withCalories(Double calories) {
            this.calories = calories;
            return this;
        }

        public Activity.Builder withCaloriesConsumed(Object caloriesConsumed) {
            this.caloriesConsumed = caloriesConsumed;
            return this;
        }

        public Activity.Builder withCaloriesEstimated(Object caloriesEstimated) {
            this.caloriesEstimated = caloriesEstimated;
            return this;
        }

        public Activity.Builder withComments(Object comments) {
            this.comments = comments;
            return this;
        }

        public Activity.Builder withCourseId(Object courseId) {
            this.courseId = courseId;
            return this;
        }

        public Activity.Builder withDecoDive(Boolean decoDive) {
            this.decoDive = decoDive;
            return this;
        }

        public Activity.Builder withDeviceId(Long deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Activity.Builder withDistance(Double distance) {
            this.distance = distance;
            return this;
        }

        public Activity.Builder withDuration(Double duration) {
            this.duration = duration;
            return this;
        }

        public Activity.Builder withElapsedDuration(Double elapsedDuration) {
            this.elapsedDuration = elapsedDuration;
            return this;
        }

        public Activity.Builder withElevationCorrected(Boolean elevationCorrected) {
            this.elevationCorrected = elevationCorrected;
            return this;
        }

        public Activity.Builder withElevationGain(Double elevationGain) {
            this.elevationGain = elevationGain;
            return this;
        }

        public Activity.Builder withElevationLoss(Double elevationLoss) {
            this.elevationLoss = elevationLoss;
            return this;
        }

        public Activity.Builder withEndLatitude(Double endLatitude) {
            this.endLatitude = endLatitude;
            return this;
        }

        public Activity.Builder withEndLongitude(Double endLongitude) {
            this.endLongitude = endLongitude;
            return this;
        }

        public Activity.Builder withFavorite(Boolean favorite) {
            this.favorite = favorite;
            return this;
        }

        public Activity.Builder withHasPolyline(Boolean hasPolyline) {
            this.hasPolyline = hasPolyline;
            return this;
        }

        public Activity.Builder withHasSplits(Boolean hasSplits) {
            this.hasSplits = hasSplits;
            return this;
        }

        public Activity.Builder withHasVideo(Boolean hasVideo) {
            this.hasVideo = hasVideo;
            return this;
        }

        public Activity.Builder withLapCount(Long lapCount) {
            this.lapCount = lapCount;
            return this;
        }

        public Activity.Builder withLocationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Activity.Builder withManualActivity(Boolean manualActivity) {
            this.manualActivity = manualActivity;
            return this;
        }

        public Activity.Builder withManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Activity.Builder withMaxBikingCadenceInRevPerMinute(Object maxBikingCadenceInRevPerMinute) {
            this.maxBikingCadenceInRevPerMinute = maxBikingCadenceInRevPerMinute;
            return this;
        }

        public Activity.Builder withMaxElevation(Double maxElevation) {
            this.maxElevation = maxElevation;
            return this;
        }

        public Activity.Builder withMaxHR(Double maxHR) {
            this.maxHR = maxHR;
            return this;
        }

        public Activity.Builder withMaxPower(Object maxPower) {
            this.maxPower = maxPower;
            return this;
        }

        public Activity.Builder withMaxSpeed(Double maxSpeed) {
            this.maxSpeed = maxSpeed;
            return this;
        }

        public Activity.Builder withMaxTemperature(Object maxTemperature) {
            this.maxTemperature = maxTemperature;
            return this;
        }

        public Activity.Builder withMaxVerticalSpeed(Double maxVerticalSpeed) {
            this.maxVerticalSpeed = maxVerticalSpeed;
            return this;
        }

        public Activity.Builder withMinActivityLapDuration(Double minActivityLapDuration) {
            this.minActivityLapDuration = minActivityLapDuration;
            return this;
        }

        public Activity.Builder withMinElevation(Double minElevation) {
            this.minElevation = minElevation;
            return this;
        }

        public Activity.Builder withMinTemperature(Object minTemperature) {
            this.minTemperature = minTemperature;
            return this;
        }

        public Activity.Builder withMovingDuration(Double movingDuration) {
            this.movingDuration = movingDuration;
            return this;
        }

        public Activity.Builder withOwnerDisplayName(String ownerDisplayName) {
            this.ownerDisplayName = ownerDisplayName;
            return this;
        }

        public Activity.Builder withOwnerFullName(String ownerFullName) {
            this.ownerFullName = ownerFullName;
            return this;
        }

        public Activity.Builder withOwnerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Activity.Builder withOwnerProfileImageUrlLarge(String ownerProfileImageUrlLarge) {
            this.ownerProfileImageUrlLarge = ownerProfileImageUrlLarge;
            return this;
        }

        public Activity.Builder withOwnerProfileImageUrlMedium(String ownerProfileImageUrlMedium) {
            this.ownerProfileImageUrlMedium = ownerProfileImageUrlMedium;
            return this;
        }

        public Activity.Builder withOwnerProfileImageUrlSmall(String ownerProfileImageUrlSmall) {
            this.ownerProfileImageUrlSmall = ownerProfileImageUrlSmall;
            return this;
        }

        public Activity.Builder withParent(Boolean parent) {
            this.parent = parent;
            return this;
        }

        public Activity.Builder withParentId(Object parentId) {
            this.parentId = parentId;
            return this;
        }

        public Activity.Builder withPr(Boolean pr) {
            this.pr = pr;
            return this;
        }

        public Activity.Builder withPurposeful(Boolean purposeful) {
            this.purposeful = purposeful;
            return this;
        }

        public Activity.Builder withSplitSummaries(List<Object> splitSummaries) {
            this.splitSummaries = splitSummaries;
            return this;
        }

        public Activity.Builder withSportTypeId(Long sportTypeId) {
            this.sportTypeId = sportTypeId;
            return this;
        }

        public Activity.Builder withStartLatitude(Double startLatitude) {
            this.startLatitude = startLatitude;
            return this;
        }

        public Activity.Builder withStartLongitude(Double startLongitude) {
            this.startLongitude = startLongitude;
            return this;
        }

        public Activity.Builder withStartTimeGMT(String startTimeGMT) {
            this.startTimeGMT = startTimeGMT;
            return this;
        }

        public Activity.Builder withStartTimeLocal(String startTimeLocal) {
            this.startTimeLocal = startTimeLocal;
            return this;
        }

        public Activity.Builder withTimeZoneId(Long timeZoneId) {
            this.timeZoneId = timeZoneId;
            return this;
        }

        public Activity.Builder withUserPro(Boolean userPro) {
            this.userPro = userPro;
            return this;
        }

        public Activity.Builder withVideoUrl(Object videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Activity.Builder withWaterConsumed(Object waterConsumed) {
            this.waterConsumed = waterConsumed;
            return this;
        }

        public Activity.Builder withWaterEstimated(Double waterEstimated) {
            this.waterEstimated = waterEstimated;
            return this;
        }

        public Activity build() {
            Activity activity = new Activity();
            activity.activityId = activityId;
            activity.activityName = activityName;
            activity.activityType = activityType;
            activity.atpActivity = atpActivity;
            activity.autoCalcCalories = autoCalcCalories;
            activity.averageBikingCadenceInRevPerMinute = averageBikingCadenceInRevPerMinute;
            activity.averageHR = averageHR;
            activity.averageSpeed = averageSpeed;
            activity.avgPower = avgPower;
            activity.beginTimestamp = beginTimestamp;
            activity.bmrCalories = bmrCalories;
            activity.calories = calories;
            activity.caloriesConsumed = caloriesConsumed;
            activity.caloriesEstimated = caloriesEstimated;
            activity.comments = comments;
            activity.courseId = courseId;
            activity.decoDive = decoDive;
            activity.deviceId = deviceId;
            activity.distance = distance;
            activity.duration = duration;
            activity.elapsedDuration = elapsedDuration;
            activity.elevationCorrected = elevationCorrected;
            activity.elevationGain = elevationGain;
            activity.elevationLoss = elevationLoss;
            activity.endLatitude = endLatitude;
            activity.endLongitude = endLongitude;
            activity.favorite = favorite;
            activity.hasPolyline = hasPolyline;
            activity.hasSplits = hasSplits;
            activity.hasVideo = hasVideo;
            activity.lapCount = lapCount;
            activity.locationName = locationName;
            activity.manualActivity = manualActivity;
            activity.manufacturer = manufacturer;
            activity.maxBikingCadenceInRevPerMinute = maxBikingCadenceInRevPerMinute;
            activity.maxElevation = maxElevation;
            activity.maxHR = maxHR;
            activity.maxPower = maxPower;
            activity.maxSpeed = maxSpeed;
            activity.maxTemperature = maxTemperature;
            activity.maxVerticalSpeed = maxVerticalSpeed;
            activity.minActivityLapDuration = minActivityLapDuration;
            activity.minElevation = minElevation;
            activity.minTemperature = minTemperature;
            activity.movingDuration = movingDuration;
            activity.ownerDisplayName = ownerDisplayName;
            activity.ownerFullName = ownerFullName;
            activity.ownerId = ownerId;
            activity.ownerProfileImageUrlLarge = ownerProfileImageUrlLarge;
            activity.ownerProfileImageUrlMedium = ownerProfileImageUrlMedium;
            activity.ownerProfileImageUrlSmall = ownerProfileImageUrlSmall;
            activity.parent = parent;
            activity.parentId = parentId;
            activity.pr = pr;
            activity.purposeful = purposeful;
            activity.splitSummaries = splitSummaries;
            activity.sportTypeId = sportTypeId;
            activity.startLatitude = startLatitude;
            activity.startLongitude = startLongitude;
            activity.startTimeGMT = startTimeGMT;
            activity.startTimeLocal = startTimeLocal;
            activity.timeZoneId = timeZoneId;
            activity.userPro = userPro;
            activity.videoUrl = videoUrl;
            activity.waterConsumed = waterConsumed;
            activity.waterEstimated = waterEstimated;
            return activity;
        }

    }

}
