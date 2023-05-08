package com.bassemHalim.cyclopath.Utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class CompositeKeyConverter implements DynamoDBTypeConverter<String, CompositeKey> {

    @Override
    public String convert(CompositeKey object) {
        return object.getPrefix() + object.getPostfix();
    }

    @Override
    public CompositeKey unconvert(String object) {
        String[] parts = object.split("#");
        return new CompositeKey(parts[0], parts[1]);
    }
}