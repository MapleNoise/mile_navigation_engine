package com.mile.mile_navigation_engine.interfaces;

import android.location.Location;

/**
 * Created by Corentin on 28/03/2019.
 */

public interface OnUserLocationFetched {
    void onSuccess(Location location);
    void onFailure(Exception e);
}
