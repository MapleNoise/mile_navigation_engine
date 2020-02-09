package com.mile.mile_navigation_engine.Navigation;

import java.util.List;

public class AppPropertiesConfiguration {

    public static String idCity = "V0";

    /**
     * BREEZOMETER
     */
    public static boolean isBreezometerFeatureActivated = true;
    public static boolean isAllergensFeatureActivated = true;

    /**
     * ROUTE
     */
    public static boolean isRouteListFiltersFeatureActivated = true;

    /**
     * LOGIN - SSO
     */
    public static boolean isDecathlonLoginActivated = true;

    /**
     * PICTURE SHARING
     */
    public static boolean isPictureSharingFeatureActivated = true;

    /**
     * NOTIFICATIONS
     */
    public static boolean isGeofenceNotificationFeatureActivated = true;

    /**
     * LOGIN
     */
    public static boolean isLoginAnonymousOnly = true;

    public static List<String> idRoutesMightBeClosed;
    public static List<String> idRoutesManualNavigation;

    static {
        //idRoutesMightBeClosed = new ArrayList<>();
        //idRoutesMightBeClosed.add("PA15");

        //idRoutesManualNavigation = new ArrayList<>();
        //idRoutesManualNavigation.add("PA15");
    }

}
