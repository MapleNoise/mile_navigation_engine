package com.mile.mile_navigation_engine_example

import androidx.annotation.NonNull;
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin2
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterFragmentActivity() {
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        flutterEngine.plugins.add(MileNavigationEnginePlugin2())
        GeneratedPluginRegistrant.registerWith(flutterEngine);
    }
}
