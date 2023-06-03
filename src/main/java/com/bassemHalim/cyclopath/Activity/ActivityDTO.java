package com.bassemHalim.cyclopath.Activity;

import com.bassemHalim.cyclopath.Weather.Weather;
import lombok.Data;

@Data
public class ActivityDTO {


    private long activityId;
    private String activityName;
    private double averageHR;
    private double averageSpeed;
    private double bmrCalories;
    private double calories;
    private double distance;
    private double duration;
    private double elapsedDuration;
    private boolean elevationCorrected;
    private double elevationGain;
    private double elevationLoss;
    private double endLatitude;
    private double endLongitude;
    private String locationName;
    private double maxElevation;
    private double maxHR;
    private double maxSpeed;
    private double maxVerticalSpeed;
    private double minActivityLapDuration;
    private double minElevation;
    private double movingDuration;
    private double startLatitude;
    private double startLongitude;
    private String startTimeGMT;
    private String startTimeLocal;
    private int timeZoneId;
    private Weather weather;
}
