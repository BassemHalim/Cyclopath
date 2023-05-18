package com.bassemHalim.cyclopath.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.bassemHalim.cyclopath.User.User;
import com.bassemHalim.cyclopath.Utils.CompositeKey;
import com.bassemHalim.cyclopath.Utils.CompositeKeyConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Cyclopath")
public class Route {
    private String CyclopathPK;
    private CompositeKey CyclopathSK;
    private long activityID;
    private byte[] geoJSON_zip;

    @DynamoDBHashKey(attributeName = "CyclopathPK")
    public String getCyclopathPK() {
        if (CyclopathPK == null) {
            CyclopathPK = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        }
        return CyclopathPK;
    }

    @DynamoDBTypeConverted(converter = CompositeKeyConverter.class)
    @DynamoDBRangeKey(attributeName = "CyclopathSK")
    public CompositeKey getCyclopathSK() {
        if (CyclopathSK == null) {
            CyclopathSK = new CompositeKey("ROUTE", String.valueOf(activityID));
        }
        return CyclopathSK;
    }
}
