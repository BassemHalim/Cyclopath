package com.bassemHalim.cyclopath.Activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


@Data
@DynamoDBTable(tableName = "Cyclopath")
@Log
public class ActivitiesMetatdata {

    private List<Long> savedActivities;
    private String UUID;
    private String SK = "METADATA";
    private List<Double> yearlyHistory;// history of activities for the current year only

    private double totalDistance = 0;
    private double totalElevGain = 0;
    private double totalTime = 0;

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


    public void addActivity(ActivityDTO activity) {
        log.info("activity: " + activity.getActivityId());
        if (savedActivities == null) {
            savedActivities = new LinkedList<>();
        }
        if (!savedActivities.contains(activity.getActivityId())) {
            savedActivities.add(activity.getActivityId());
            // update yearlyHistory
            totalDistance += activity.getDistance();
            totalTime += activity.getDuration();
            totalElevGain += activity.getElevationGain();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(activity.getStartTimeLocal(), formatter);

            if (dateTime.getYear() == Year.now().getValue()) {
                List<Double> arr = getYearlyHistory();
                int doy = dateTime.getDayOfYear();
                arr.set(doy, arr.get(doy) + activity.getDistance());
            }
        }
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

    public boolean hasActivity(Long ID) {
        return savedActivities.contains(ID);
    }


    public List<Double> getYearlyHistory() {
        if (yearlyHistory == null || yearlyHistory.size() == 0) {
            yearlyHistory = new ArrayList<>(Collections.nCopies(366, 0.0));
        }
        return yearlyHistory;
    }

}
