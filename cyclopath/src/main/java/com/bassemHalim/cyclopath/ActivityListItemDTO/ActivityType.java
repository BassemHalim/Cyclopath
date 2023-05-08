package com.bassemHalim.cyclopath.ActivityListItemDTO;

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