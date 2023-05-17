package com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityDTO;

import lombok.Data;

import java.util.List;

@Data

public class MetadataDTO {
    private Object diveNumber;
    //    private DeviceMetaDataDTO deviceMetaDataDTO;
    private String lastUpdateDate;
    private Object eBikeAssistModeInfoDTOList;
    private int lapCount;
    private boolean gcj02;
    private String uploadedDate;
    private Object agentApplicationInstallationId;
    private String manufacturer;
    private Object eBikeBatteryRemaining;
    private boolean elevationCorrected;
    private Object videoUrl;
    private boolean hasChartData;
    private boolean hasHrTimeInZones;
    private List<Object> childIds;
    private List<Object> childActivityTypes;
    private boolean hasPowerTimeInZones;
    private boolean hasSplits;
    private boolean hasIntensityIntervals;
    private boolean personalRecord;
    private List<Object> activityImages;
    private Object runPowerWindDataEnabled;
    private Object eBikeBatteryUsage;
    private int deviceApplicationInstallationId;
    private Object associatedWorkoutId;
    private boolean hasPolyline;
    private Object eBikeMaxAssistModes;
    private Object calendarEventInfo;
    private UserInfoDto userInfoDto;
    private boolean isOriginal;
    private Object associatedCourseId;
    private List<Object> sensors;
    private Object agentString;
    private boolean autoCalcCalories;
    private Object isAtpActivity;
    private boolean trimmed;
    private boolean manualActivity;
    private boolean favorite;
//    private FileFormat fileFormat;
}