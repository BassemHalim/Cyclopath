package com.bassemHalim.cyclopath.Repositoy;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import com.bassemHalim.cyclopath.Activity.ActivitiesMetatdata;
import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.Map.Route;
import com.bassemHalim.cyclopath.User.User;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;

@Repository
@RequiredArgsConstructor
@Log
public class SingleTableDB {

    private final DynamoDBMapper dynamoDBMapper;

    // ----------------User-------------------
    public void saveUser(User user) {
        boolean success = false;
        do {
            try {
                dynamoDBMapper.save(user);
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
    }

    public User getUserByEmail(String Email) {
        User user = new User();
        user.setEmail(Email);
        DynamoDBQueryExpression<User> queryExpression =
                new DynamoDBQueryExpression<User>()
                        .withHashKeyValues(user)
                        .withLimit(2) // to verify uniqueness
                        .withIndexName("email-index")
                        .withConsistentRead(false);

        List<User> users = dynamoDBMapper.query(User.class, queryExpression);
        if (users.size() > 0) {
            // assumes emails are unique
            return users.get(0);
        }
        return null;
    }

    public User getUserByUUID(String uuid) {
        return dynamoDBMapper.load(User.class, uuid);
    }

    public void deleteUser(String uuid) {
        User tmp = getUserByUUID(uuid);
        dynamoDBMapper.delete(tmp);
    }

    //------------------Activity------------------
    public void saveActivity(Activity activity) {
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
    }

    public void batchSaveActivities(List<Activity> activityList) {
        log.log(new LogRecord(Level.INFO, "saving " + activityList.size() + " activities"));

//        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
//                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER).build();

        boolean success = false;
        do {
            try {
                dynamoDBMapper.batchSave(activityList);
                success = true;
            } catch (ProvisionedThroughputExceededException e) {
                System.out.println("exceeded provisioned WCU going to sleep for a bit");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ie) {
                    log.severe("sleep interrupted");
                    Thread.currentThread().interrupt();
                }
            }
        } while (!success);
    }

    /**
     * get at most the num_activities most recent activities of the user with UUID
     *
     * @param num_activities, UUID
     * @return a list of the most recent <num_activities> activities
     */
    public List<Activity> batchGetActivity(@Positive int num_activities, String UUID) {
//        Activity activity = new Activity();
//        activity.setOwnerUUID(UUID);
        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put(":uuid", new AttributeValue().withS(UUID));
        attributeValueMap.put(":activity", new AttributeValue().withS("ACTIVITY#"));
        DynamoDBQueryExpression<Activity> queryExpression =
                new DynamoDBQueryExpression<Activity>()
                        .withLimit(num_activities)
                        .withScanIndexForward(true)
                        .withKeyConditionExpression("CyclopathPK = :uuid AND begins_with(CyclopathSK, :activity)")
                        .withExpressionAttributeValues(attributeValueMap);

        QueryResultPage<Activity> queryPage = dynamoDBMapper.queryPage(Activity.class, queryExpression, DynamoDBMapperConfig.builder().build());
        List<Activity> activityList = queryPage.getResults();
        return activityList;
    }

    public Activity getActivity(String PK, CompositeKey SK) {
        return dynamoDBMapper.load(Activity.class, PK, SK);
    }

    public void deleteActivity(String PK, CompositeKey SK) {
        Activity activity = getActivity(PK, SK);
        if (activity != null)
            dynamoDBMapper.delete(activity);
    }


    public void updateActivity(Long ID, Activity activity) {
        // @TODO fix this
        dynamoDBMapper.save(activity,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("activityID",
                                new ExpectedAttributeValue(new AttributeValue().withS(ID.toString()))
                        ));

    }

    public void saveActivitiesMetadata(ActivitiesMetatdata activity) {
        dynamoDBMapper.save(activity);
    }

    /**
     * tries to retires activities metadata, if not found it creates a new one w/o saving it to DB
     *
     * @param UUID
     * @return ActivitiesMetadata is found else null
     */
    public ActivitiesMetatdata getActivityMetadata(String UUID) {
        ActivitiesMetatdata activitiesMetatdata = null;
        try {
            activitiesMetatdata = dynamoDBMapper.load(ActivitiesMetatdata.class, UUID, "METADATA");
        } catch (Exception e) {
            log.severe(e.toString());
        }
        return activitiesMetatdata;
    }

    //----------------ROUTE--------------------------
    public void saveRoute(Route route) {
        boolean success = false;
        do {
            try {
                dynamoDBMapper.save(route);
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
    }

    public Route getRoute(String PK, CompositeKey SK) {

        return dynamoDBMapper.load(Route.class, PK, SK);
    }
}
