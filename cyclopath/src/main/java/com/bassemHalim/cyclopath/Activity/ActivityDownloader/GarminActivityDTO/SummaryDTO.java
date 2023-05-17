package com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityDTO;

import lombok.Data;

@Data
public class SummaryDTO {
    private double maxHR;
    private double distance;
    private double minActivityLapDuration;
    private double minElevation;
    private double movingDuration;
    private double elevationGain;
    private double averageSpeed;
    private double maxSpeed;
    private double calories;
    private double endLatitude;
    private double maxVerticalSpeed;
    private double duration;
    private double startLatitude;
    private String startTimeGMT;
    private double averageMovingSpeed;
    private double waterEstimated;
    private double elevationLoss;
    private double endLongitude;
    private double averageHR;
    private double startLongitude;
    private String startTimeLocal;
    private double maxElevation;
    private double bmrCalories;
    private double elapsedDuration;
}
