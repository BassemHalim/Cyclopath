package com.bassemHalim.cyclopath.Activity;


import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityDTO.GarminActivityDTO;
import com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityListItemDTO.ActivityListItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    ActivityMapper MAPPER = Mappers.getMapper(ActivityMapper.class);

    @Mapping(source = "weather", target = "weather")
    ActivityDTO toDTO(Activity activity);

    ActivityDTO toDTO(ActivityListItemDTO activityListItemDTOctivityListItem);

    List<ActivityDTO> ActivitytoDTOList(List<Activity> c);

    List<ActivityDTO> ActivityListItemDTOtoDTOList(List<ActivityListItemDTO> list);


    @Mapping(source = "activityId", target = "activityId")
    @Mapping(source = "activityName", target = "activityName")
    @Mapping(source = "summaryDTO.maxHR", target = "maxHR")
    @Mapping(source = "summaryDTO.distance", target = "distance")
    @Mapping(source = "summaryDTO.minActivityLapDuration", target = "minActivityLapDuration")
    @Mapping(source = "summaryDTO.minElevation", target = "minElevation")
    @Mapping(source = "summaryDTO.movingDuration", target = "movingDuration")
    @Mapping(source = "summaryDTO.elevationGain", target = "elevationGain")
    @Mapping(source = "summaryDTO.averageSpeed", target = "averageSpeed")
    @Mapping(source = "summaryDTO.maxSpeed", target = "maxSpeed")
    @Mapping(source = "summaryDTO.calories", target = "calories")
    @Mapping(source = "summaryDTO.endLatitude", target = "endLatitude")
    @Mapping(source = "summaryDTO.maxVerticalSpeed", target = "maxVerticalSpeed")
    @Mapping(source = "summaryDTO.duration", target = "duration")
    @Mapping(source = "summaryDTO.startLatitude", target = "startLatitude")
    @Mapping(source = "summaryDTO.startTimeGMT", target = "startTimeGMT")
    @Mapping(source = "summaryDTO.waterEstimated", target = "waterEstimated")
    @Mapping(source = "summaryDTO.elevationLoss", target = "elevationLoss")
    @Mapping(source = "summaryDTO.endLongitude", target = "endLongitude")
    @Mapping(source = "summaryDTO.averageHR", target = "averageHR")
    @Mapping(source = "summaryDTO.startLongitude", target = "startLongitude")
    @Mapping(source = "summaryDTO.startTimeLocal", target = "startTimeLocal")
    @Mapping(source = "summaryDTO.maxElevation", target = "maxElevation")
    @Mapping(source = "summaryDTO.bmrCalories", target = "bmrCalories")
    @Mapping(source = "summaryDTO.elapsedDuration", target = "elapsedDuration")
    @Mapping(source = "timeZoneUnitDTO.unitId", target = "timeZoneId")
    @Mapping(source = "metadataDTO.lapCount", target = "lapCount")
    @Mapping(source = "metadataDTO.hasSplits", target = "hasSplits")
    @Mapping(source = "metadataDTO.hasPolyline", target = "hasPolyline")
    @Mapping(source = "metadataDTO.manualActivity", target = "manualActivity")
    @Mapping(source = "metadataDTO.manufacturer", target = "manufacturer")
    Activity toEntity(GarminActivityDTO garminActivityDTO);
}
