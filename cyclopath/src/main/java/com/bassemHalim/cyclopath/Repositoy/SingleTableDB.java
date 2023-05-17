package com.bassemHalim.cyclopath.Repositoy;

import com.bassemHalim.cyclopath.Activity.ActivitiesMetatdata;
import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.User.User;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log
public class SingleTableDB {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final String TABLE_NAME = "Cyclopath";


    // ----------------User-------------------
    public void saveUser(User user) {
        boolean success = false;
        do {
            try {
                dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class)).putItem(user);
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

    public User getUserByEmail(String email) {
        User user = dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class))
                .execute(r -> r.query(q -> q.keyConditionExpression("email = :val")
                        .expressionValues(Collections.singletonMap(":val", AttributeValue.builder().s(email).build()))))
                .items()
                .stream()
                .findFirst()
                .orElse(null);

        return user;
    }

    public User getUserByUUID(String uuid) {
        return dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class)).getItem(r -> r.key(k -> k.partitionValue(uuid)));
    }

    public void deleteUser(String uuid) {
        User user = getUserByUUID(uuid);
        if (user != null) {
            dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class)).deleteItem(r -> r.key(k -> k.partitionValue(uuid)));
        }
    }

    //------------------Activity------------------
    public void saveActivity(Activity activity) {
        boolean success = false;
        do {
            try {
                dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Activity.class)).putItem(activity);
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
        WriteBatch.builder(Activity.class)
                .mappedTableResource(dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Activity.class)))
                .writeRequests(activityList.stream()
                        .map(activity -> PutItemEnhancedRequest.builder(Activity.class).item(activity).build())
                        .collect(Collectors.toList()))
                .build()
                .execute();
    }


    public Activity getActivityById(String pk, CompositeKey sk) {
        return dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Activity.class))
                .getItem(r -> r.key(k -> k.partitionValue(pk).sortValue(sk)));
    }


    public void deleteActivity(String pk, CompositeKey sk) {
        Activity activity = getActivityById(pk, sk);
        if (activity != null) {
            dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Activity.class))
                    .deleteItem(r -> r.key(k -> k.partitionValue(pk).sortValue(sk)));
        }
    }


    public void updateActivity(Long id, Activity activity) {
        dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Activity.class))
                .putItem(activity, r -> r.conditionExpression("activityID = :val")
                        .expressionValues(Collections.singletonMap(":val", AttributeValue.builder().n(id.toString()).build())));
    }


    public void saveActivitiesMetadata(ActivitiesMetatdata activity) {
        dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(ActivitiesMetatdata.class)).putItem(activity);
    }

    /**
     * tries to retires activities metadata, if not found it creates a new one w/o saving it to DB
     *
     * @param UUID
     * @return ActivitiesMetadata is found else null
     */
    public ActivitiesMetatdata getActivityMetadata(String UUID) {
        return dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(ActivitiesMetatdata.class))
                .getItem(r -> r.key(k -> k.partitionValue(UUID).sortValue("METADATA")));
    }

}
