package com.bassemHalim.cyclopath.Activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;


@Data
@DynamoDBTable(tableName = "Cyclopath")
public class ActivitiesMetatdata {

    private List<Long> savedActivities;

    private String UUID;
    private String SK = "METADATA";

    @DynamoDBHashKey(attributeName = "CyclopathPK")
    public String getUUID() {
        return UUID;
    }

    @DynamoDBRangeKey(attributeName = "CyclopathSK")
    public String getSK() {
        return SK;
    }


    public void setSK(String key) {
        SK = key;
    }


    public void addActivity(Long activityId) {
        if (savedActivities == null) {
            savedActivities = new LinkedList<>();
        }
        savedActivities.add(activityId);
    }

    public List<Long> getSavedActivities() {
        if (savedActivities == null) {
            savedActivities = new LinkedList<>();
        }
        return savedActivities;
    }

    public void removeActivity(Long id) {
        savedActivities.remove(id);
    }
}
