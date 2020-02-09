package com.mile.mile_navigation_engine.Navigation.utils;

import android.location.Location;

public class CoordinateUtils {
    static public ProjectionUtils projectOnNewReference (Location previousLocation, Location actuelLocation, Location poiLocation) {
        //All coordinates from CLLocation are written with reference 0
        //All coordinates projected in OA vector abscisse are written with reference 1

        Double xO0 = previousLocation.getLatitude();
        Double yO0 = previousLocation.getLongitude();
        Double xA0 = actuelLocation.getLatitude();
        Double yA0 = actuelLocation.getLongitude();
        Double xB0 = poiLocation.getLatitude();
        Double yB0 = poiLocation.getLongitude();
        //We need to move our axes
        xA0 = xA0-xO0;
        yA0 = yA0-yO0;
        xB0 = xB0-xO0;
        yB0 = yB0-yO0;
        xO0 = 0d;
        yO0 = 0d;
        Double OA = Math.sqrt( Math.pow( xA0 , 2 ) + Math.pow( yA0 , 2 ) );
        Double xOA = xA0-xO0;
        Double yOA = yA0-yO0;

        Double cosAlpha = (xOA / OA);
        Double sinAlpha = (yOA / OA);
        Double xA1 = cosAlpha*xA0 + sinAlpha*yA0;
        Double yB1 = (-sinAlpha*xB0) + (cosAlpha*yB0);
        Double xB1 = cosAlpha*xB0 + sinAlpha*yB0;


        Location parentCoordinateInNewRefence = new Location("");
        parentCoordinateInNewRefence.setLatitude(xB1);
        parentCoordinateInNewRefence.setLongitude(yB1);

        Boolean isOnPlot = false;
        if (Math.abs(xB1)< Math.abs(xA1)){
            isOnPlot = xB1 > xO0;
        }

        //Projection of B onto OA. In 0 reference
        Double xP0 = previousLocation.getLatitude() + cosAlpha*xB1;
        Double yP0 = previousLocation.getLongitude() + (sinAlpha*xB1);

        Location projectionCoordinate = new Location("");
        projectionCoordinate.setLatitude(xP0);
        projectionCoordinate.setLongitude(yP0);

        //  Double xP0 = cosAlpha*xB1 +  previousLocation.getLatitude();
        //  Double yP0 = sinAlpha*yB1 + previousLocation.getLongitude();

        //  CoordinateOrthogonal coordinates = new CoordinateOrthogonal(xP0,yP0);


        ProjectionUtils result = new ProjectionUtils(projectionCoordinate,parentCoordinateInNewRefence,isOnPlot);

        return result;


    }
}