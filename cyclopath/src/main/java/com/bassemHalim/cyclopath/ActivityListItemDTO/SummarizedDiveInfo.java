package com.bassemHalim.cyclopath.ActivityListItemDTO;

import lombok.Data;

import java.util.List;

@Data
public class SummarizedDiveInfo {
    private Object surfaceCondition;
    private Object current;
    private Object visibility;
    private Object visibilityUnit;
    private List<Object> summarizedDiveGases;
    private Object weight;
    private Object totalSurfaceTime;
    private Object waterDensity;
    private Object waterType;
    private Object weightUnit;
}