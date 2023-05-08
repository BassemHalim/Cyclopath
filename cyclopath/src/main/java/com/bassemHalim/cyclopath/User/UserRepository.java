package com.bassemHalim.cyclopath.User;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void save(User user) {
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
                        .withIndexName("email")
                        .withConsistentRead(false);

        List<User> users = dynamoDBMapper.query(User.class, queryExpression);
        if (users.size() > 1) {
            System.out.println("found more than 1 user with email: " + Email);
        }
        // assumes emails are unique
        user = users.get(0);
        return user;
    }

    public User getUserByUUID(String uuid) {
        return dynamoDBMapper.load(User.class, uuid);
    }

    public void delete(String uuid) {
        User tmp = getUserByUUID(uuid);
        dynamoDBMapper.delete(tmp);
    }

//    public void update(Long ID, User user) {
//        // @TODO fix this
//        dynamoDBMapper.save(activity,
//                new DynamoDBSaveExpression()
//                        .withExpectedEntry("activityID",
//                                new ExpectedAttributeValue(new AttributeValue().withS(ID.toString()))
//                        ));
//
//    }

}
