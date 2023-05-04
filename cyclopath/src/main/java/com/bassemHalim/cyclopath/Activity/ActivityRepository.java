package com.bassemHalim.cyclopath.Activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class ActivityRepository {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Activity save(Activity activity) {
        boolean success = false;
        do {
            try {
                dynamoDBMapper.save(activity);
                success = true;
            } catch (ProvisionedThroughputExceededException e) {
                System.out.println("exceeded provisioned WCU going to sleep for a bit");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        } while (!success);
        return activity;
    }

    public Activity getActivityById(Long ID) {
        return dynamoDBMapper.load(Activity.class, ID);
    }

    public void delete(Long ID) {
        Activity tmp = getActivityById(ID);
        dynamoDBMapper.delete(tmp);
    }

    public void update(Long ID, Activity activity) {
        // @TODO fix this
        dynamoDBMapper.save(activity,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("activityID",
                                new ExpectedAttributeValue(new AttributeValue().withS(ID.toString()))
                        ));

    }


}
