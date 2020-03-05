package com.mile.mile_navigation_engine


import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mile.mile_navigation_engine.utils.AppDataHolder
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.concurrent.atomic.AtomicInteger

class MileNavigationEnginePlugin2: FlutterPlugin, ActivityAware, Application.ActivityLifecycleCallbacks {

    private var _methodChannel: MethodChannel? = null
    private var _eventChannel: EventChannel? = null
    private var _activePOIChannel: EventChannel? = null
    private lateinit var _activity: Activity
    private lateinit var _context: Context
    private lateinit var _messenger: BinaryMessenger
    private lateinit var binding: FlutterPlugin.FlutterPluginBinding

    private val state = AtomicInteger(0)
    private var registrarActivityHashCode = 0

    companion object {
        val CREATED = 1
        val STARTED = 2
        val RESUMED = 3
        val PAUSED = 4
        val STOPPED = 5
        val DESTROYED = 6

        @Suppress("unused")
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val instance = MileNavigationEnginePlugin2()
            instance.onAttachedToEngine(registrar.activeContext(), registrar.messenger())
        }
    }


    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        this.binding = binding
        onAttachedToEngine(binding.applicationContext, binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        _methodChannel!!.setMethodCallHandler(null)
        _methodChannel = null
        _eventChannel!!.setStreamHandler(null)
        _eventChannel = null
        _activePOIChannel!!.setStreamHandler(null)
        _activePOIChannel = null
    }


    private fun onAttachedToEngine(binding: Context, messenger: BinaryMessenger) {
        _context = binding
        _methodChannel = MethodChannel(messenger, "flutter_mapbox_navigation")
        _eventChannel = EventChannel(messenger, "flutter_mapbox_navigation/arrival")
        _activePOIChannel = EventChannel(messenger, "flutter_mapbox_navigation/active_poi")
        _messenger = messenger
    }

    override fun onDetachedFromActivity() {
        _activity.finish()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        _activity = binding.activity
        _activity.application.registerActivityLifecycleCallbacks(this)
        this.MileNavigationEnginePlugin2()
        var factory = MileNavigationEngineFactory(this.state, _context, _activity as FragmentActivity, _messenger)
        this.binding.platformViewRegistry.registerViewFactory("navigation_view", factory)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityPaused(activity: Activity?) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        state.set(PAUSED)
    }

    override fun onActivityResumed(activity: Activity?) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        state.set(RESUMED)
    }

    override fun onActivityStarted(activity: Activity?) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        state.set(STARTED)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        state.set(DESTROYED)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        state.set(STOPPED)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        state.set(CREATED)
    }

    private fun MileNavigationEnginePlugin2() {
        registrarActivityHashCode = _activity.hashCode()
    }
}