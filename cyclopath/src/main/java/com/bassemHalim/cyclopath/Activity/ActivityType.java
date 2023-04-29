
package com.bassemHalim.cyclopath.Activity;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Generated;

@Generated("net.hexar.json2pojo")
public class ActivityType {

    @Expose
    private Boolean isHidden;
    @Expose
    private Long parentTypeId;
    @Expose
    private Boolean restricted;
    @Expose
    private Object sortOrder;
    @Expose
    private Boolean trimmable;
    @Expose
    private Long typeId;
    @Expose
    private String typeKey;

    public Boolean getIsHidden() {
        return isHidden;
    }

    public Long getParentTypeId() {
        return parentTypeId;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public Object getSortOrder() {
        return sortOrder;
    }

    public Boolean getTrimmable() {
        return trimmable;
    }

    public Long getTypeId() {
        return typeId;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public static class Builder {

        private Boolean isHidden;
        private Long parentTypeId;
        private Boolean restricted;
        private Object sortOrder;
        private Boolean trimmable;
        private Long typeId;
        private String typeKey;

        public ActivityType.Builder withIsHidden(Boolean isHidden) {
            this.isHidden = isHidden;
            return this;
        }

        public ActivityType.Builder withParentTypeId(Long parentTypeId) {
            this.parentTypeId = parentTypeId;
            return this;
        }

        public ActivityType.Builder withRestricted(Boolean restricted) {
            this.restricted = restricted;
            return this;
        }

        public ActivityType.Builder withSortOrder(Object sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public ActivityType.Builder withTrimmable(Boolean trimmable) {
            this.trimmable = trimmable;
            return this;
        }

        public ActivityType.Builder withTypeId(Long typeId) {
            this.typeId = typeId;
            return this;
        }

        public ActivityType.Builder withTypeKey(String typeKey) {
            this.typeKey = typeKey;
            return this;
        }

        public ActivityType build() {
            ActivityType activityType = new ActivityType();
            activityType.isHidden = isHidden;
            activityType.parentTypeId = parentTypeId;
            activityType.restricted = restricted;
            activityType.sortOrder = sortOrder;
            activityType.trimmable = trimmable;
            activityType.typeId = typeId;
            activityType.typeKey = typeKey;
            return activityType;
        }

    }

}
