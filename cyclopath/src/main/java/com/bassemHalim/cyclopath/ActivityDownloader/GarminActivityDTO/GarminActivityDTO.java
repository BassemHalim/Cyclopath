package com.bassemHalim.cyclopath.ActivityDownloader.GarminActivityDTO;

import lombok.Data;

@Data

public class GarminActivityDTO {
    private long activityId;
    private MetadataDTO metadataDTO;
    private String locationName;
    private TimeZoneUnitDTO timeZoneUnitDTO;
    private String activityName;
    private SummaryDTO summaryDTO;
    private int userProfileId;
    private ActivityUUID activityUUID;


}
