package com.bassemHalim.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.bassemHalim.Activity.Activity;
import com.bassemHalim.Utils.CompositeKey;
import com.bassemHalim.Utils.CompositeKeyConverter;
import com.bassemHalim.User.User;
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
        if (CyclopathSK == null || CyclopathSK.getPostfix().startsWith("8") || CyclopathSK.getPostfix()
                .startsWith("9")) { //@FIXME remove
            CyclopathSK = new CompositeKey("ROUTE", Activity.getCompositeKeyPostfix(activityID));
        }
        return CyclopathSK;
    }
}
