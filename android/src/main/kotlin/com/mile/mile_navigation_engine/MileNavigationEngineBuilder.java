package com.mile.mile_navigation_engine;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;


public class MileNavigationEngineBuilder {

    MileNavigationEngine2 build(int id, Context context, FragmentActivity activity, String route, String gpsColor, String accessToken, String mode, AtomicInteger state, BinaryMessenger messenger) {
        final MileNavigationEngine2 controller = new MileNavigationEngine2(id, context, messenger, activity, route, gpsColor, accessToken, mode, state);
        controller.init();
        return controller;
    }

}
