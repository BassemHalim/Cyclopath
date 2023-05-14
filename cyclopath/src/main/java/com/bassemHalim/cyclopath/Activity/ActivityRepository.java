//package com.bassemHalim.cyclopath.Activity;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
//import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Repository
//public class ActivityRepository {
//    @Autowired
//    private DynamoDBMapper dynamoDBMapper;
//
//    public void saveUser(Activity activity) {
//        boolean success = false;
//        do {
//            try {
//                dynamoDBMapper.saveUser(activity);
//                success = true;
//            } catch (ProvisionedThroughputExceededException e) {
//                System.out.println("exceeded provisioned WCU going to sleep for a bit");
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        } while (!success);
//    }
//
//    public void batchSave(List<Activity> activityList) {
//        boolean success = false;
//        do {
//            try {
//                dynamoDBMapper.batchSave(activityList);
//                success = true;
//            } catch (ProvisionedThroughputExceededException e) {
//                System.out.println("exceeded provisioned WCU going to sleep for a bit");
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        } while (!success);
//    }
//
//    public Activity getActivityById(Long ID) {
//        return dynamoDBMapper.load(Activity.class, ID);
//    }
//
//    public void deleteUser(Long ID) {
//        Activity tmp = getActivityById(ID);
//        dynamoDBMapper.deleteUser(tmp);
//    }
//
//    public void update(Long ID, Activity activity) {
//        // @TODO fix this
//        dynamoDBMapper.saveUser(activity,
//                new DynamoDBSaveExpression()
//                        .withExpectedEntry("activityID",
//                                new ExpectedAttributeValue(new AttributeValue().withS(ID.toString()))
//                        ));
//
//    }
//
//
//}
