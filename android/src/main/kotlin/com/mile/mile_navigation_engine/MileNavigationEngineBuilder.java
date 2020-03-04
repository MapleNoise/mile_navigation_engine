package com.mile.mile_navigation_engine;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.atomic.AtomicInteger;


public class MileNavigationEngineBuilder {
    public final String TAG = getClass().getSimpleName();

    MileNavigationEngine2 build(Context context, FragmentActivity activity, String route, String gpsColor, String accessToken, String mode,  AtomicInteger state) {
        final MileNavigationEngine2 controller = new MileNavigationEngine2(context, activity, route, gpsColor, accessToken, mode, state);
        controller.init();
        return controller;
    }

}
