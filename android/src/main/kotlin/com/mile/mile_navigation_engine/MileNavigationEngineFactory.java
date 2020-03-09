package com.mile.mile_navigation_engine;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.mapbox.mapboxsdk.camera.CameraPosition;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class MileNavigationEngineFactory extends PlatformViewFactory {

    private final Context _context;
    private final FragmentActivity _activity;
    private final BinaryMessenger _messenger;

    private final AtomicInteger mActivityState;

    public MileNavigationEngineFactory(AtomicInteger state, Context context, FragmentActivity activity, BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        _context = context;
        _activity = activity;
        mActivityState = state;
        _messenger = messenger;
    }

    @Override
    public PlatformView create(Context context, int id, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        final MileNavigationEngineBuilder builder = new MileNavigationEngineBuilder();
        String route = "";
        String gpsColor = "";
        String accessToken = "";
        String mode = "";
        if (params.containsKey("route")) {
            route = (String) params.get("route");
        }
        if (params.containsKey("gpsColor")) {
            gpsColor = (String) params.get("gpsColor");
        }
        if (params.containsKey("accessToken")) {
            accessToken = (String) params.get("accessToken");
        }
        if (params.containsKey("mode")) {
            mode = (String) params.get("mode");
        }

        if (mode == NavigationMode.Companion.getNAVIGATE_IN_ROUTE()) {
            return builder.buildNavigation(id, _context, _activity, route, gpsColor, accessToken, mode, mActivityState, _messenger);
        } else {
            return builder.buildNavigationToPOI(id, _context, _activity, route, gpsColor, accessToken, mode, mActivityState, _messenger);
        }

    }
}
