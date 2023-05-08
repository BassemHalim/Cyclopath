package com.bassemHalim.cyclopath.ActivityDownloader.GarminActivityDTO;

import lombok.Data;

@Data
public class TimeZoneUnitDTO {
    private String unitKey;
    private int unitId;
    private String timeZone;
    private double factor;
}
