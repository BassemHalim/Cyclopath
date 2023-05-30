package com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityListItemDTO;

import lombok.Data;

@Data
public class ActivityType {
    private boolean trimmable;
    private String typeKey;
    private int parentTypeId;
    private boolean restricted;
    private Object sortOrder;
    private int typeId;
    private boolean isHidden;
}