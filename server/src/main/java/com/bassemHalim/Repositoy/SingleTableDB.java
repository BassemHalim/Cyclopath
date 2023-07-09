package com.bassemHalim.Repositoy;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.bassemHalim.Activity.ActivitiesMetatdata;
import com.bassemHalim.Activity.Activity;
import com.bassemHalim.Map.Route;
import com.bassemHalim.Utils.CompositeKey;
import com.bassemHalim.User.User;
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
        // TODO handle user not found differently do not return null
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
        if (activityList.size() == 0) return;
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

    public void batchDeleteActivities(List<Activity> objectsToDelete) {
        log.log(new LogRecord(Level.INFO, "deleting " + objectsToDelete.size() + " activities"));

        boolean success = false;
        do {
            try {
                List<DynamoDBMapper.FailedBatch> failedBatch = dynamoDBMapper.batchDelete(objectsToDelete);
                failedBatch.forEach(o -> log.info(o.getException().getMessage()));
                success = true;
            } catch (ProvisionedThroughputExceededException e) {
                System.out.println("exceeded provisioned WCU going to sleep for a bit");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ie) {
                    log.severe("sleep interrupted");
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                log.warning(e.getMessage());
            }
        } while (!success);
    }

    /**
     * get at most the num_activities most recent activities of the user with UUID
     *
     * @param num_activities  num activities to retrieve
     * @param startActivityID activity id of last retrieved activity to use as offest use 0 to start from beginning
     * @return a list of the most recent <num_activities> activities
     */
    public List<Activity> batchGetActivity(@Positive int num_activities, String UUID, long startActivityID) {
        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put(":uuid", new AttributeValue().withS(UUID));
        attributeValueMap.put(":activity", new AttributeValue().withS("ACTIVITY#"));

        Map<String, AttributeValue> startKey = null;
        if (startActivityID != 0) {
            startKey = new HashMap();
            startKey.put("CyclopathPK", new AttributeValue(UUID));
            startKey.put("CyclopathSK", new AttributeValue("ACTIVITY#" + startActivityID));
        }

        DynamoDBQueryExpression<Activity> queryExpression =
                new DynamoDBQueryExpression<Activity>()
                        .withLimit(num_activities)
                        .withScanIndexForward(false)
                        .withKeyConditionExpression("CyclopathPK = :uuid AND begins_with(CyclopathSK, :activity)")
                        .withExpressionAttributeValues(attributeValueMap)
                        .withExclusiveStartKey(startKey);

        QueryResultPage<Activity> queryPage = dynamoDBMapper.queryPage(Activity.class,
                                                                       queryExpression,
                                                                       DynamoDBMapperConfig.builder().build());
        return queryPage.getResults();
    }


    public Activity getActivity(String PK, CompositeKey SK) {
        try {
            return dynamoDBMapper.load(Activity.class, PK, SK);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Activity not found");
        }
    }

    public void deleteActivity(String PK, CompositeKey SK) {
        Activity activity = getActivity(PK, SK);
        if (activity != null)
            dynamoDBMapper.delete(activity);
    }


    public void updateActivity(Long ID, Activity activity) {
        // @fixme
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
     * tries to fetch activities metadata from db, if not found it creates a new one w/o saving it to DB
     *
     * @param UUID user UUID
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
        log.info("saving route");
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

    public void batchSaveRoute(List<Route> routeList) {
        if (routeList.size() == 0) return;
        log.log(new LogRecord(Level.INFO, "saving " + routeList.size() + " routes"));

//        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
//                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER).build();

        boolean success = false;
        do {
            try {
                dynamoDBMapper.batchSave(routeList);
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

    public void batchSaveAndDeleteRoute(List<Route> routesToSave, List<Route> routesToDelete) {
        log.log(new LogRecord(Level.INFO, "saving " + routesToSave.size() + " routes"));
        log.log(new LogRecord(Level.INFO, "deleting " + routesToDelete.size() + " routes"));

//        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
//                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER).build();

        boolean success = false;
        do {
            try {
                dynamoDBMapper.batchSave(routesToSave, routesToDelete);
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

    public Route getRoute(String PK, CompositeKey SK) {

        return dynamoDBMapper.load(Route.class, PK, SK);
    }

    /**
     * used to update all activities CK to left pad with 0 ex: ACTIVITY#8154485949 to ACTIVITY#08154485949
     */
    public void updateRouteCompositeKeys(String UUID) {
        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put(":uuid", new AttributeValue().withS(UUID));
        attributeValueMap.put(":route", new AttributeValue().withS("ROUTE#8"));
        DynamoDBQueryExpression<Route> queryExpression =
                new DynamoDBQueryExpression<Route>()
                        .withLimit(50)
                        .withScanIndexForward(true)
                        .withKeyConditionExpression("CyclopathPK = :uuid AND begins_with(CyclopathSK, :route)")
                        .withExpressionAttributeValues(attributeValueMap);
        try {
            QueryResultPage<Route> queryPage = dynamoDBMapper.queryPage(Route.class,
                                                                        queryExpression,
                                                                        DynamoDBMapperConfig.builder().build());
            List<Route> routeList = queryPage.getResults();
            routeList.stream().forEach(s -> System.out.println(s.getActivityID()));
//            batchDeleteActivities(activityList);
            batchSaveRoute(routeList);
            // delete later
        } catch (Exception e) {
            log.warning(e.getMessage());
        }

    }
}
