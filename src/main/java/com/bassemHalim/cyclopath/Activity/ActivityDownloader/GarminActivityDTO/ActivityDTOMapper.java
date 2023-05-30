package com.bassemHalim.cyclopath.Activity.ActivityDownloader.GarminActivityDTO;

import com.bassemHalim.cyclopath.Activity.Activity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ActivityDTOMapper implements Function<GarminActivityDTO, Activity> {
    @Override
    public Activity apply(GarminActivityDTO dto) {
//        SummaryDTO summaryDTO = dto.getSummaryDTO();
//        MetadataDTO metadataDTO = dto.getMetadataDTO();
//        return new Activity(dto.getActivityId(),
//                dto.getActivityName(),
//                summaryDTO.getAverageHR(),
//                summaryDTO.getAverageSpeed(),
//                summaryDTO.getBmrCalories(),
//                summaryDTO.getCalories(),
//                summaryDTO.getDistance(),
//                summaryDTO.getDuration(),
//                summaryDTO.getElapsedDuration(),
//                metadataDTO.isElevationCorrected(),
//                summaryDTO.getElevationGain(),
//                summaryDTO.getElevationLoss(),
//                summaryDTO.getEndLatitude(),
//                summaryDTO.getEndLongitude(),
//                metadataDTO.isHasPolyline(),
//                metadataDTO.isHasSplits(),
//                metadataDTO.getLapCount(),
//                dto.getLocationName(),
//                metadataDTO.isManualActivity(),
//                metadataDTO.getManufacturer(),
//                summaryDTO.getMaxElevation(),
//                summaryDTO.getMaxHR(),
//                summaryDTO.getMaxSpeed(),
//                summaryDTO.getMaxVerticalSpeed(),
//                summaryDTO.getMinActivityLapDuration(),
//                summaryDTO.getMinElevation(),
//                summaryDTO.getMovingDuration(),
//                dto.getUserProfileId(),
//                summaryDTO.getStartLatitude(),
//                summaryDTO.getStartLongitude(),
//                summaryDTO.getStartTimeGMT(),
//                summaryDTO.getStartTimeLocal(),
//                dto.getTimeZoneUnitDTO().getUnitId(),
//                summaryDTO.getWaterEstimated(),
//                null
//        );
        return null;
    }

}
