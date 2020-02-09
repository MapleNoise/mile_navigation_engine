package com.mile.mile_navigation_engine.utils.Geofence;


import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class GeofenceConstants {

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";
    public static final int RADIUS_DETECT_GEOFENCES = 200000; // value in meters
    public static final int RADIUS_GEOFENCE_CITY = 20000; // TODO get value directly from database to be more accurate
    public static final int MAX_GEOFENCES_PER_DEVICE = 100; // data found from official google documentation - do not exceed this value
    public static final int RADIUS_GEOFENCE_USER = 40000; // radius around user
    public static final int INTERVAL_GEOFENCE_REFRESH_LOCATION = 3600000;

}
