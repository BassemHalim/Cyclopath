package com.bassemHalim.cyclopath.Weather;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

@Data
@DynamoDBDocument
public class Weather {
    public String issueDate;
    public int temp;
    public int apparentTemp;
    public int dewPoint;
    public int relativeHumidity;
    public int windDirection;
    @DynamoDBAttribute(attributeName = "CompassPoint")
    public String windDirectionCompassPoint;
    public int windSpeed;
    public int windGust;
    public double latitude;
    public double longitude;
}
/*
{
            "issueDate":"2023-05-11T22:53:00.000+0000",
            "temp":68,
            "apparentTemp":68,
            "dewPoint":52,
            "relativeHumidity":56,
            "windDirection":260,
            "windDirectionCompassPoint":"w",
            "windSpeed":9,
            "windGust":null,
            "latitude":33.6756999604404,
            "longitude":-117.86799995228648,
            "weatherStationDTO":{"id":"KSNA","name":"John Wayne Airport-Orange County","timezone":null},
            "weatherTypeDTO":{"weatherTypePk":null,"desc":"Mostly Clear","image":null}
}
 */

