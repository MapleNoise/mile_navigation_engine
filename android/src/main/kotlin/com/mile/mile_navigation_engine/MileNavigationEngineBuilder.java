package com.mile.mile_navigation_engine;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.BinaryMessenger;


public class MileNavigationEngineBuilder {

    MileNavigationEngine buildNavigation(int id, Context context, Activity activity, String route, String gpsColor, String accessToken, String mode, AtomicInteger state, BinaryMessenger messenger) {
        final MileNavigationEngine controller = new MileNavigationEngine(id, context, messenger, activity, route, gpsColor, accessToken, mode, state);
        controller.init();
        return controller;
    }

    MileNavigationToPOIEngine buildNavigationToPOI(int id, Context context, Activity activity, String route, String gpsColor, String accessToken, String mode, AtomicInteger state, BinaryMessenger messenger) {
        final MileNavigationToPOIEngine controller = new MileNavigationToPOIEngine(id, context, messenger, activity, route, gpsColor, accessToken, mode, state);
        controller.init();
        return controller;
    }

}
