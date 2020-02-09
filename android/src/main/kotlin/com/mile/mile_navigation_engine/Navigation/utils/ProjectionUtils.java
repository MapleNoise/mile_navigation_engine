package com.mile.mile_navigation_engine.Navigation.utils;

import android.location.Location;

public class ProjectionUtils {


    private Location projectionCoordinate;
    private Location parentProjectionCoordinateInNewReference;
    private Boolean isOnPlot;


    public ProjectionUtils(Location coordinates, Location parentProjection, Boolean isOnPlot) {
        this.projectionCoordinate = coordinates;
        this.parentProjectionCoordinateInNewReference = parentProjection;
        this.isOnPlot = isOnPlot;
    }

    public Boolean getOnPlot() {
        return isOnPlot;
    }

    public Location getProjectionCoordinate() {
        return projectionCoordinate;
    }

    public Location getParentProjectionCoordinateInNewReference() {
        return parentProjectionCoordinateInNewReference;
    }
}
