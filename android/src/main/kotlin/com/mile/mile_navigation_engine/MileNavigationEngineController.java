package com.mile.mile_navigation_engine;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.platform.PlatformView;

public class MileNavigationEngineController implements MethodChannel.MethodCallHandler, PlatformView {

    private final int id;
    private final AtomicInteger activityState;
    private final MethodChannel methodChannel;
    private final PluginRegistry.Registrar registrar;
    private final int registrarActivityHashCode;
    private final Context context;

    MileNavigationEngineController(int id, Context context, AtomicInteger activityState, PluginRegistry.Registrar registrar) {

        this.id = id;
        this.context = context;
        this.activityState = activityState;
        this.registrar = registrar;
        methodChannel = new MethodChannel(registrar.messenger(), "flutter_mapbox_navigation" + id);
        methodChannel.setMethodCallHandler(this);
        this.registrarActivityHashCode = registrar.activity().hashCode();
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {

    }

    @Override
    public View getView() {
        return new WebView(context);
    }

    @Override
    public void dispose() {

    }
}
