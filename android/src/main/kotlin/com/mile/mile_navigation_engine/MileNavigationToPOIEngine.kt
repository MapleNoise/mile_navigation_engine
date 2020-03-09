package com.mile.mile_navigation_engine

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.LayerDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.devzone.fillprogresslayout.FillProgressLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.matching.v5.MapboxMapMatching
import com.mapbox.api.matching.v5.models.MapMatchingResponse
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.mapbox.mapboxsdk.style.layers.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.services.android.navigation.ui.v5.camera.DynamicCamera
import com.mapbox.services.android.navigation.ui.v5.camera.NavigationCamera
import com.mapbox.services.android.navigation.ui.v5.camera.NavigationCameraUpdate
import com.mapbox.services.android.navigation.ui.v5.instruction.InstructionView
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener
import com.mapbox.services.android.navigation.ui.v5.map.NavigationMapboxMap
import com.mapbox.services.android.navigation.ui.v5.voice.NavigationSpeechPlayer
import com.mapbox.services.android.navigation.ui.v5.voice.SpeechAnnouncement
import com.mapbox.services.android.navigation.ui.v5.voice.SpeechPlayerProvider
import com.mapbox.services.android.navigation.ui.v5.voice.VoiceInstructionLoader
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine
import com.mapbox.services.android.navigation.v5.milestone.Milestone
import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener
import com.mapbox.services.android.navigation.v5.milestone.VoiceInstructionMilestone
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation
import com.mapbox.services.android.navigation.v5.navigation.NavigationConstants
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress
import com.mapbox.services.android.navigation.v5.utils.DistanceFormatter
import com.mapbox.services.android.navigation.v5.utils.RouteUtils
import com.mapbox.turf.TurfMeasurement
import com.mapbox.turf.TurfTransformation
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin.Companion.CREATED
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin.Companion.DESTROYED
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin.Companion.PAUSED
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin.Companion.RESUMED
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin.Companion.STARTED
import com.mile.mile_navigation_engine.MileNavigationEnginePlugin.Companion.STOPPED
import com.mile.mile_navigation_engine.Navigation.AppConfiguration
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.Navigation.service.AudioService
import com.mile.mile_navigation_engine.Navigation.utils.IntentUtils
import com.mile.mile_navigation_engine.activities.navigation.NavigationActivity
import com.mile.mile_navigation_engine.interfaces.MP3Listener
import com.mile.mile_navigation_engine.model.*
import com.mile.mile_navigation_engine.utils.*
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import me.grantland.widget.AutofitTextView
import okhttp3.Cache
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MileNavigationToPOIEngine : Application.ActivityLifecycleCallbacks,
        MethodChannel.MethodCallHandler,
        PlatformView,
        MapboxMap.OnMapLongClickListener,
        OnMapReadyCallback,
        MapboxMap.OnMapClickListener,
        ProgressChangeListener,
        MilestoneEventListener,
        OffRouteListener,
        NavigationListener,
        MP3Listener {

    var _activity: FragmentActivity
    var _context: Context
    private var disposed = false
    private var activityState: AtomicInteger? = null
    private var registrarActivityHashCode = 0

    private var mainView: View

    private var mapReadyResult: MethodChannel.Result? = null

    var _distanceRemaining: Double? = null
    var _durationRemaining: Double? = null

    var route = ""
    var gpsColor = ""
    var accessToken = ""
    var mode = ""

    private var methodChannel: MethodChannel? = null

    var id: Int = -1

    var PERMISSION_REQUEST_CODE: Int = 367

    constructor(id: Int, context: Context, messenger: BinaryMessenger, activity: FragmentActivity, route: String, gpsColor: String, accessToken: String, mode: String, state: AtomicInteger) {
        this.id = id
        this._context = context
        this._activity = activity;
        this.route = route
        this.gpsColor = gpsColor
        this.accessToken = accessToken
        this.mode = mode
        this.activityState = state
        this.registrarActivityHashCode = _activity.hashCode()

        this.methodChannel = MethodChannel(messenger, "flutter_mapbox_navigation_"+id)
        this.methodChannel!!.setMethodCallHandler(this)

        _activity.setTheme(R.style.CustomInstructionView)
        Mapbox.getInstance(_activity, accessToken)
        flutterRouteToKtRoute(route, gpsColor)

        var inflater = _activity.layoutInflater;
        mainView = inflater.inflate(R.layout.activity_component_navigation, null)

        OfflineRouteManager.fetchTileVersions(_context)

        this.callback = MileNavigationToPOIEngine.ComponentActivityLocationCallback(this)

        mapView = view.findViewById(R.id.mapView)

        navigationLayout = view.findViewById(R.id.componentNavigationLayout)
        instructionView = view.findViewById(R.id.instructionView)
        instructionView.findViewById<TextView>(R.id.stepPrimaryText).textSize = 23f
        instructionView.findViewById<TextView>(R.id.stepPrimaryText).maxLines = 3

        componentNavigationLayout = view.findViewById( R.id.componentNavigationLayout );
        //toolbar = view.findViewById( R.id.toolbar );
        instructionViewNoNavigation = view.findViewById( R.id.instructionViewNoNavigation );
        maneuverImage = view.findViewById( R.id.maneuver_image );
        maneuverInstruction = view.findViewById( R.id.maneuver_instruction );
        coordinatorBannerPoi = view.findViewById( R.id.coordinator_banner_poi );
        containerGooddeal = view.findViewById( R.id.container_gooddeal );
        imageBannerPoi = view.findViewById( R.id.image_banner_poi );
        titleBannerPoi = view.findViewById( R.id.title_banner_poi );
        descriptionBannerPoi = view.findViewById( R.id.description_banner_poi );
        linkGooddeal = view.findViewById( R.id.link_gooddeal );
        menuLayout = view.findViewById( R.id.menu_layout );
        statsLayout = view.findViewById( R.id.stats_layout );
        distanceContainer = view.findViewById( R.id.distance_container );
        distanceLeftTextview = view.findViewById( R.id.distance_left_textview );
        timerTextview = view.findViewById( R.id.timer_textview );
        speedTextview = view.findViewById( R.id.speed_textview );
        startNavigationHolder = view.findViewById( R.id.start_navigation_holder );
        navigationStateButton = view.findViewById( R.id.navigation_state_button );
        resumeButton = view.findViewById( R.id.resume_button );
        finishButton = view.findViewById( R.id.finish_button );
        gpsLoaderHolder = view.findViewById( R.id.gps_loader_holder );
        textLoader = view.findViewById( R.id.text_loader );
        progressBarLoader = view.findViewById( R.id.progress_bar_loader );
        bottomSheetPoiLayout = view.findViewById( R.id.bottom_sheet_poi_layout );
        titlePoiTextview = view.findViewById( R.id.title_poi_textview );
        imagePoiImageview = view.findViewById( R.id.image_poi_imageview );
        descriptionPoiTextview = view.findViewById( R.id.description_poi_textview );
        poiPlayer = view.findViewById( R.id.poi_player );
        playPoiButton = view.findViewById( R.id.play_poi_button );
        stopPoiButton = view.findViewById( R.id.stop_poi_button );
        mp3Fillprogresslayout = view.findViewById( R.id.mp3_fillprogresslayout );
        mp3TimeTextview = view.findViewById( R.id.mp3_time_textview );
        mp3FillprogresslayoutStatic = view.findViewById( R.id.mp3_fillprogresslayout_static );
        mp3TimeTextviewStatic = view.findViewById( R.id.mp3_time_textview_static );
        bottomSheetRate = view.findViewById( R.id.bottom_sheet_rate );
        titleStatsResume = view.findViewById( R.id.title_stats_resume );
        statsLayoutBs = view.findViewById( R.id.stats_layout_bs );
        icFlagVariantOutline = view.findViewById( R.id.ic_flag_variant_outline );
        lengthTextview = view.findViewById( R.id.length_textview );
        totalDistanceTravelledTextview = view.findViewById( R.id.total_distance_travelled_textview );
        icTimer = view.findViewById( R.id.ic_timer );
        durationTextview = view.findViewById( R.id.duration_textview );
        totalTimeTextview = view.findViewById( R.id.total_time_textview );
        icSpeed = view.findViewById( R.id.ic_speed );
        speedTextviewStatic = view.findViewById( R.id.speed_textview_static );
        averageSpeedTextview = view.findViewById( R.id.average_speed_textview );
        titleExperienceResume = view.findViewById( R.id.title_experience_resume );
        rating = view.findViewById( R.id.rating );
        closeNavigation = view.findViewById( R.id.close_navigation );
        recenterButton = view.findViewById( R.id.recenter_button );

        mapView.getMapAsync(this)

        AudioService.getInstance().init(_context, _activity.application)

        //initToolbar()
        initView()
        initListeners()

        showNavigationHolderLayout(false)
        //manageVisibilityMenuLayout(false)
        showStatsLayout(false)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        var arguments = call.arguments as? Map<String, Any>

        when (call.method) {
            "map#waitForMap" -> {

                if (::mapboxMap.isInitialized) {
                    result.success(null);
                    return;
                }
                mapReadyResult = result;
            }
            else -> throw IllegalArgumentException(
                    "Cannot interpret " + activityState!!.get() + " as an activity state");
        }
    }

    fun flutterRouteToKtRoute(routeJson: String?, gpsColor: String?) {

        //For parse automatically massive params
        var gson = Gson()
        var route = gson?.fromJson(routeJson, RouteDataClass::class.java)

        //For parse specific params
        val routeJSONObj = JSONObject(routeJson)
        if(routeJSONObj.has("methodLocomotion")){
            val methodLocomotion = routeJSONObj.get("methodLocomotion") as Integer
            route.methodLocomotion = RouteDataClass.Locomotion.getLocomotion(methodLocomotion.toLong())
        }

        if(routeJSONObj.has("arrayCoordinates")){
            var locations: ArrayList<Location> = ArrayList()
            val arrayCoordinates = routeJSONObj.get("arrayCoordinates") as JSONArray

            for (i in 0 until arrayCoordinates.length()) {
                val item = arrayCoordinates.getJSONObject(i)
                var location = Location("provider")
                location.latitude = (item.get("latitude")as Double?)!!
                location.longitude = (item.get("longitude") as Double?)!!
                locations.add(location)
            }
            route.arrayCoordinates = locations
            route.previewCoordinates = locations //Maybe redo
            AppDataHolder.currentStartingPoint = StartingPoint("", locations.get(0).latitude, locations.get(0).longitude, 0, 0) //Maybe redo
        } else {
            route.arrayCoordinates = ArrayList()
        }

        if(routeJSONObj.has("arrayDescription")){
            val arrayDescription = HashMap<LanguageCode, String>()
            val arrayDescriptionObj = routeJSONObj.get("arrayDescription") as JSONObject
            LanguageCode.values().forEach {
                if(arrayDescriptionObj.has(it.getLocaleLanguage().toUpperCase())) {
                    var description = arrayDescriptionObj.get(it.getLocaleLanguage().toUpperCase())
                    arrayDescription[it] = description as String
                }
            }
            route.arrayDescription = arrayDescription
        }

        if(routeJSONObj.has("arrayName")){
            val arrayName = HashMap<LanguageCode, String>()
            val arrayNameObj = routeJSONObj.get("arrayName") as JSONObject
            LanguageCode.values().forEach {
                if(arrayNameObj.has(it.getLocaleLanguage().toUpperCase())) {
                    var name = arrayNameObj.get(it.getLocaleLanguage().toUpperCase())
                    arrayName[it] = name as String
                }
            }
            route.arrayName = arrayName
        }

        if(routeJSONObj.has("routeCenter")){
            val coor = routeJSONObj.get("routeCenter") as JSONObject
            val location = Location("")
            location.latitude = coor.get("latitude") as Double
            location.longitude = coor.get("longitude") as Double
            var rCenter = RouteCenter(location, (routeJSONObj.get("routeRadius") as Integer).toLong());
            route.center = rCenter;
        }

        if(routeJSONObj.has("arrayPois")){
            var pois: ArrayList<POI> = ArrayList()
            val arrayPois = routeJSONObj.get("arrayPois") as JSONArray

            for (i in 0 until arrayPois.length()) {
                val poiObj = arrayPois.getJSONObject(i)
                var poi = gson?.fromJson(poiObj.toString(), POI::class.java)

                if(poiObj.has("arrayCoordinates")){
                    var locations: ArrayList<Location> = ArrayList()
                    val arrayCoordinates = poiObj.get("arrayCoordinates") as JSONArray

                    for (i in 0 until arrayCoordinates.length()) {
                        val item = arrayCoordinates.getJSONObject(i)
                        var location = Location("provider")
                        location.latitude = (item.get("latitude")as Double?)!!
                        location.longitude = (item.get("longitude") as Double?)!!
                        locations.add(location)
                    }
                    poi.arrayCoordinates = locations
                }

                if(poiObj.has("type")){
                    val type = poiObj.get("type") as Integer
                    poi.type = POI.Type.get(type.toLong())
                }

                if(poiObj.has("arrayDescription")){
                    val arrayDescription = HashMap<LanguageCode, String>()
                    val arrayDescriptionObj = poiObj.get("arrayDescription") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayDescriptionObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var description = arrayDescriptionObj.get(it.getLocaleLanguage().toUpperCase())
                            arrayDescription[it] = description as String
                        }
                    }
                    poi.arrayDescription = arrayDescription
                }

                if(poiObj.has("arrayName")){
                    val arrayName = HashMap<LanguageCode, String>()
                    val arrayNameObj = poiObj.get("arrayName") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayNameObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var name = arrayNameObj.get(it.getLocaleLanguage().toUpperCase())
                            arrayName[it] = name as String
                        }
                    }
                    poi.arrayName = arrayName
                }

                if(poiObj.has("arrayMP3")){
                    val array = HashMap<LanguageCode, String>()
                    val arrayObj = poiObj.get("arrayMP3") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var description = arrayObj.get(it.getLocaleLanguage().toUpperCase())
                            array[it] = description as String
                        }
                    }
                    poi.arrayMP3 = array
                }

                if(poiObj.has("arrayTextMP3")){
                    val array = HashMap<LanguageCode, String>()
                    val arrayObj = poiObj.get("arrayTextMP3") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var description = arrayObj.get(it.getLocaleLanguage().toUpperCase())
                            array[it] = description as String
                        }
                    }
                    poi.arrayTextMP3 = array
                }

                pois.add(poi)
                route.arrayPois = pois
            }
        } else {
            route.arrayPois = ArrayList()
        }

        AppDataHolder.currentRoute = route
        AppDataHolder.gpsColor = gpsColor
        //AppDataHolder.applicationInstance = this.application as FlutterApplication
    }

    companion object {

        private val FIRST = 0
        private val ONE_HUNDRED_MILLISECONDS = 100
        private val BOTTOMSHEET_PADDING_MULTIPLIER = 4
        public val TWO_SECONDS_IN_MILLISECONDS = 2000
        private val BEARING_TOLERANCE = 90.0
        private val SEARCHING_FOR_GPS_MESSAGE = "Searching for GPS..."
        private val GPS_FOUND_MESSAGE = "GPS found, have a nice run!"
        private val COMPONENT_NAVIGATION_INSTRUCTION_CACHE =
                "component-navigation-instruction-cache"
        private val TEN_MEGABYTE_CACHE_SIZE = (10 * 1024 * 1024).toLong()
        private val ZERO_PADDING = 0
        public val DEFAULT_ZOOM = 12.0
        public val DEFAULT_TILT = 0.0
        private val DEFAULT_BEARING = 0.0
        private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000
        private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 500
        private val INSTRUCTION_DURATION_DISPLAY: Long = 5000

        private val DEPARTURE_MARKER_SOURCE = "departure_marker"
        private val ARRIVAL_MARKER_SOURCE = "arrival_marker"
        private val LENGTH_ANIMATION_GOODDEAL_MS = 10000

        private val INTERVAL_BETWEEN_SAVES = 5000

        fun navigateFrom(context : Context){
            context.startActivity(Intent(context, NavigationActivity::class.java))
        }
    }

    lateinit var navigationLayout: CoordinatorLayout
    lateinit var mapView: MapView
    lateinit var mapboxMap : MapboxMap
    lateinit var instructionView: InstructionView

    private lateinit var callback: LocationEngineCallback<LocationEngineResult>
    private var locationEngine: LocationEngine? = null
    private var navigation: MapboxNavigation? = null
    private var speechPlayer: NavigationSpeechPlayer? = null
    private var navigationMap: NavigationMapboxMap? = null
    private var lastLocation: Location? = null
    private var previousLocation: Location? = null
    private var calculatedRouteRun: DirectionsRoute? = null
    private var calculatedRouteLeadUserToRun: DirectionsRoute? = null
    private var destination: Point? = null
    private var mapState: MapState? = null
    private var loadedStyle: Style? = null
    private lateinit var annotationManager : SymbolManager
    private lateinit var circleManager: CircleManager
    private lateinit var displayedAnnotations : List<Symbol>
    private var displayedFeatures = ArrayList<Feature>()
    private var hasRunStarted = false
    private var hasRouteFinished = false
    private var hasLeadStarted = false
    private var hasLeadBeenRequested = false
    private var isUserTooFarFromRoute = false
    private var isMapboxSpeakerMuted = false
    private var currentBlockerPOI : POI? = null
    private var hasUserRequestedNavigation = false
    private var hasUserRequestedMockNavigation = false
    private var hasUserChangedRating = false
    private var isMockNavigationPaused = false
    private var currentMockNavigationSpeed = 20
    private var closestRoutePoint: Point? = null
    private var currentDisplayedPOI: POI? = null
    private var startNavTimestamp = 0L
    private var currentDirectionRoute : DirectionsRoute? = null

    // STATS
    private var startingLocation : Location? = null
    private var initDistanceToTravel = 0.0
    private var totalDistanceTravelled = 0.0
    private var totalTimeTravelled = 0L
    private var averageSpeed = 0f
    private var listSpeeds = ArrayList<Float>()
    private var currentRunSession: RunSession? = null

    private var audioService = AudioService.getInstance()

    private var bottomSheetPoiBehavior : BottomSheetBehavior<LinearLayout>? = null
    private var bottomSheetEndBehavior : BottomSheetBehavior<LinearLayout>? = null
    private var navigationState = NavigationState.WAITING_TO_START
    private var timeStampStartGooddealAnimation: Long = 0
    private var isGoodDealDisplayed = false
    private var idGoodDealDisplayed: String? = null

    private var totalTimeInMS = 0L
    private var previousTime = 0L
    private var lastTimeUserHasBeenOffRoute = 0L
    private var previousSaveTimeStamp = 0L

    private var componentNavigationLayout: CoordinatorLayout? = null
    private var  toolbar:Toolbar?= null
    private var  instructionViewNoNavigation: LinearLayout?= null
    private var  maneuverImage: ImageView?= null
    private var  maneuverInstruction: AutofitTextView? = null
    private var  coordinatorBannerPoi: CoordinatorLayout?= null
    private var  containerGooddeal: RelativeLayout?= null
    private var  imageBannerPoi:ImageView? = null
    private var  titleBannerPoi: TextView?= null
    private var  descriptionBannerPoi:TextView? = null
    private var  linkGooddeal: FloatingActionButton?= null
    private var  menuLayout: LinearLayout?= null
    private var  statsLayout:LinearLayout? = null
    private var  distanceContainer:LinearLayout? = null
    private var  distanceLeftTextview: AutofitTextView? = null
    private var  timerTextview: AutofitTextView? = null
    private var  speedTextview: AutofitTextView? = null
    private var  startNavigationHolder:LinearLayout? = null
    private var  navigationStateButton:Button? = null
    private var  resumeButton:Button? = null
    private var  finishButton:Button? = null
    private var  gpsLoaderHolder:LinearLayout? = null
    private var  textLoader:TextView? = null
    private var  progressBarLoader:ProgressBar? = null
    private var  bottomSheetPoiLayout:LinearLayout? = null
    private var  titlePoiTextview: TextView?= null
    private var  imagePoiImageview: ImageView?= null
    private var  descriptionPoiTextview:TextView? = null
    private var  poiPlayer:LinearLayout? = null
    private var  playPoiButton:Button? = null
    private var  stopPoiButton:Button? = null
    private var  mp3Fillprogresslayout: FillProgressLayout? = null
    private var  mp3TimeTextview:TextView? = null
    private var  mp3FillprogresslayoutStatic: FillProgressLayout? = null
    private var  mp3TimeTextviewStatic:TextView? = null
    private var  bottomSheetRate:LinearLayout? = null
    private var  titleStatsResume:TextView? = null
    private var  statsLayoutBs:LinearLayout? = null
    private var  icFlagVariantOutline:ImageView? = null
    private var  lengthTextview:TextView? = null
    private var  totalDistanceTravelledTextview:TextView? = null
    private var  icTimer:ImageView? = null
    private var  durationTextview:TextView? = null
    private var  totalTimeTextview:TextView?= null
    private var  icSpeed:ImageView? = null
    private var  speedTextviewStatic:TextView? = null
    private var  averageSpeedTextview:TextView? = null
    private var  titleExperienceResume:TextView? = null
    private var  rating: AppCompatRatingBar? = null
    private var closeNavigation:Button? = null
    private var recenterButton: FloatingActionButton? = null

    private enum class MapState {
        INFO,
        NAVIGATION
    }

    private enum class NavigationState {
        WAITING_TO_START,
        IN_PROGRESS,
        PAUSED,
        FINISHED
    }

    private var MARKER_SOURCE = "markers-source-custom-"
    private var RADIUS_MARKER_SOURCE = "radius-markers-source-custom-"
    private var INVISIBLE_SOURCE = "invisible-source"
    private var MARKER_STYLE_LAYER = "markers-style-layer-"
    private var RADIUS_MARKER_STYLE_LAYER = "radius-markers-style-layer-"
    private var INVISIBLE_LAYER = "invisible-style"
    private var MARKER_IMAGE = "custom-marker"


    override fun getView(): View {
        return mainView;
    }

    private fun initView(){
        initBottomSheets()
        bottomSheetPoiBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetEndBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        //bottomSheetEndBehavior?.isHideable = false
        recenterButton!!.hide()
        finishButton!!.visibility = View.GONE
        resumeButton!!.visibility = View.GONE
        initInstructionView()
        showCustomInstructionView(false)
        // TODO Improve distance algorithm computing when navigation is deactivated
        if(!AppDataHolder.currentRoute?.isNavigationActivated!!){
            distanceContainer!!.visibility = View.GONE
        }
    }

    private fun initListeners() {
        navigationStateButton!!.setOnClickListener {
            if(navigationState == NavigationState.WAITING_TO_START){

                val arguments: MutableMap<String, Any> = java.util.HashMap(1)
                arguments["isStarted"] = true
                methodChannel!!.invokeMethod("onNavigationStarted", arguments)

                hasUserRequestedNavigation = true
                manageNavigationState()
                showLoadingGPS(true, _activity.getString(R.string.message_generating_route))
                //StatisticManager.routeStarted(currentRoute!!)
                //if(checkIfUserIsTooFarFromRoute(lastLocation)){
                leadUserToRunRoute()
                //}else{
                //startRunNavigation(MapUtils.computeRouteCoordinatesBasedOnClosestPoint(lastLocation!!, AppDataHolder.currentRoute!!.arrayCoordinates!!))
                //}
            }else if(navigationState == NavigationState.IN_PROGRESS){
                manageNavigationState()
                navigationState = NavigationState.PAUSED
                //moveCameraOverhead()
                navigationMap!!.showRouteOverview(intArrayOf(150,150,150,150))
            }
        }

        resumeButton!!.setOnClickListener {
            if(navigationState == NavigationState.PAUSED){
                manageNavigationState()
                navigationMap!!.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS)
                val cameraUpdate = cameraOverheadUpdate()
                if (cameraUpdate != null) {
                    val navUpdate = NavigationCameraUpdate(cameraUpdate)
                    navigationMap!!.retrieveCamera().update(navUpdate)
                }
                navigationState = NavigationState.IN_PROGRESS
            }
        }

        finishButton!!.setOnClickListener {
            finishNavigation(false)
        }

        recenterButton!!.setOnClickListener {
            recenterButton!!.hide()
            navigationMap!!.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS)
        }

        closeNavigation!!.setOnClickListener {
            val arguments: MutableMap<String, Any> = java.util.HashMap(1)
            arguments["isFinished"] = false //TODO 75%
            methodChannel!!.invokeMethod("onNavigationFinished", arguments)
        }
    }

    private fun initBottomSheets(){
        bottomSheetPoiBehavior = BottomSheetBehavior.from(bottomSheetPoiLayout)
        bottomSheetPoiBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        recenterButton!!.show()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        recenterButton!!.hide()
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        recenterButton!!.hide()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        recenterButton!!.show()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        bottomSheetEndBehavior = BottomSheetBehavior.from(bottomSheetRate)
        bottomSheetEndBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        recenterButton!!.hide()
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        recenterButton!!.hide()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        bottomSheetPoiLayout!!.setOnClickListener {

        }

        playPoiButton!!.setOnClickListener {
            if(currentDisplayedPOI?.arrayMP3!!.isEmpty())
                audioService.playTTS(SpeechAnnouncement.builder()
                        .announcement(currentDisplayedPOI?.description)
                        .build(), false)
            else
                audioService.playDescriptionMP3(currentDisplayedPOI!!, this)

            playPoiButton!!.visibility = View.GONE
            stopPoiButton!!.visibility = View.VISIBLE
            mp3Fillprogresslayout!!.visibility = View.VISIBLE
            Handler().postDelayed({
                mp3FillprogresslayoutStatic!!.visibility = View.GONE
            }, 750)
        }

        stopPoiButton!!.setOnClickListener {
            audioService.stop()

            playPoiButton!!.visibility = View.VISIBLE
            stopPoiButton!!.visibility = View.GONE
            mp3Fillprogresslayout!!.visibility = View.GONE
            mp3FillprogresslayoutStatic!!.visibility = View.VISIBLE

            this.onListeningFinished()
        }
    }

    override fun dispose() {

        if (disposed) {
            return
        }
        disposed = true

        if (circleManager != null) {
            circleManager.onDestroy()
        }
        mapboxMap.locationComponent.isLocationComponentEnabled = false

        mapView.onDestroy()
        audioService.stopAudioService(false)

        // Ensure proper shutdown of the SpeechPlayer
        if (speechPlayer != null) {
            speechPlayer!!.onDestroy()
        }

        // Prevent leaks
        removeLocationEngineListener()

        (navigation!!.cameraEngine as DynamicCamera).clearMap()

        // MapboxNavigation will shutdown the LocationEngine
        navigation!!.onDestroy()
        _activity.application.unregisterActivityLifecycleCallbacks(this)
    }


    private fun showNavigationHolderLayout(visible: Boolean){
        if(visible) startNavigationHolder!!.visibility = View.VISIBLE
        else startNavigationHolder!!.visibility = View.GONE
    }

    private fun showVisibilityToolbar(visible: Boolean){
        /*if(visible) toolbar!!.visibility = View.VISIBLE
        else toolbar!!.visibility = View.GONE*/
    }

    private fun initMap(){
        mapboxMap.uiSettings.setCompassFadeFacingNorth(false)
        mapboxMap.uiSettings.isLogoEnabled = false
        mapboxMap.uiSettings.isAttributionEnabled = false
        var arrayLatLng = MapUtils.convertPointsToLatLng(MapUtils.convertCoordinatesToPoints(AppDataHolder.currentRoute?.arrayCoordinates!!))
        if(lastLocation != null)
            arrayLatLng.add(LatLng(lastLocation?.latitude!!, lastLocation?.longitude!!))


        val latLngBounds = LatLngBounds.Builder()
                .includes(arrayLatLng)
                .build()

        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50))
    }

    /**
     * Enabling location symbol on the map if possible
     */
    private fun enableLocationComponent(loadedMapStyle: Style) {

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(_context)) {

            // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(_context)
                    .trackingGesturesManagement(true)
                    .accuracyColor(ContextCompat.getColor(_context, R.color.colorPrimaryDark))
                    .compassAnimationEnabled(true)
                    .layerAbove(INVISIBLE_LAYER)
                    .elevation(4f)
                    .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(_context, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .locationEngine(locationEngine)
                    .build()

            // Get an instance of the LocationComponent and then adjust its settings
            mapboxMap.locationComponent.apply {

                // Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

                // Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

                // Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING_GPS_NORTH

                // Set the LocationComponent's render mode
                renderMode = RenderMode.GPS

                moveCameraOverhead()
            }
        } else {
            PermissionManager.checkIfLocationIsGranted(_activity)

            //permissionsManager = PermissionsManager(this)
            //permissionsManager.requestLocationPermissions(this)
        }
    }

    /**
     * Init MapBox instruction view with user settings like language (for metrics)
     */
    private fun initInstructionView(){
        if(AppDataHolder.currentRoute != null && AppDataHolder.currentRoute!!.isNavigationActivated!!){
            val roundingIncrement = NavigationConstants.ROUNDING_INCREMENT_TWENTY_FIVE
            val distanceFormatter = DistanceFormatter(_context, ApplicationRunner.appLanguage.getLocaleLanguage(), ApplicationRunner.appDistanceMetrics.toString(), roundingIncrement)
            instructionView.setDistanceFormatter(distanceFormatter)

            instructionView.setInstructionListListener { visible ->
                if(visible) showRecenterButton(true)
                else showRecenterButton(false)
            }
        }else{
            instructionView.visibility = View.GONE
        }
    }

    /**
     * Displays a custom instruction view in case MapBox navigation is disabled - some routes does not use MapBox navigation
     */
    private fun showCustomInstructionView(show: Boolean){
        if(show) instructionViewNoNavigation!!.visibility = View.VISIBLE
        else instructionViewNoNavigation!!.visibility = View.GONE
    }
    /**
     * Callback once map is ready and loaded - we start to do things
     */
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        if (mapReadyResult != null) {
            mapReadyResult!!.success(null)
            mapReadyResult = null
        }
        this.mapboxMap.addOnMapClickListener(this)
        mapboxMap.setStyle(Style.Builder().fromUri(_activity.getString(R.string.map_uri))) { style ->

            initMap()
            circleManager = CircleManager(mapView, mapboxMap, style)
            loadedStyle = style
            mapState = MapState.INFO
            navigationMap = NavigationMapboxMap(mapView, mapboxMap)

            mapboxMap.addOnMoveListener(object: MapboxMap.OnMoveListener{
                override fun onMoveBegin(detector: MoveGestureDetector) {
                }

                override fun onMove(detector: MoveGestureDetector) {
                    if(hasLeadStarted || hasRunStarted){
                        if(bottomSheetPoiBehavior?.state == BottomSheetBehavior.STATE_HIDDEN) // If POI bottom sheet is visible, we don't show it
                            recenterButton!!.show()
                    }
                }

                override fun onMoveEnd(detector: MoveGestureDetector) {
                }

            })

            displayRouteOnMap()
            displayMarkersOnMap()

            // We add POI images on map
            displayPOIS(style)

            // For Location updates
            initializeLocationEngine()

            initLocalizationPlugin(mapView, mapboxMap, style)

            style.addImage(DEPARTURE_MARKER_SOURCE, _activity.resources.getDrawable(R.drawable.start_flag))
            style.addImage(ARRIVAL_MARKER_SOURCE, _activity.resources.getDrawable(R.drawable.finishflag_night_3x))

            setFlutterCustomColor()

            // For annotations
            //initAnnotationManager(mapView, mapboxMap, style)

            //style.addLayer(SymbolLayer())

            // For navigation logic / processing
            initializeNavigation(mapboxMap)
            //initObservers()

            //navigationMap!!.updateCameraTrackingMode(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS)
            //navigationMap!!.updateLocationLayerRenderMode(RenderMode.GPS)

            // For voice instructions
            initializeSpeechPlayer()
        }
    }

    private fun setFlutterCustomColor() {
        menuLayout!!.setBackgroundColor(Color.parseColor(AppDataHolder.gpsColor))
        startNavigationHolder!!.setBackgroundColor(Color.parseColor(AppDataHolder.gpsColor))
        gpsLoaderHolder!!.setBackgroundColor(Color.parseColor(AppDataHolder.gpsColor))
        instructionView!!.setBackgroundColor(Color.parseColor(AppDataHolder.gpsColor))
        titlePoiTextview!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        descriptionPoiTextview!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        titleStatsResume!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        titleExperienceResume!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        closeNavigation!!.setBackgroundColor(Color.parseColor(AppDataHolder.gpsColor))
        icSpeed!!.setColorFilter(Color.parseColor(AppDataHolder.gpsColor))
        averageSpeedTextview!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        speedTextviewStatic!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        icTimer!!.setColorFilter(Color.parseColor(AppDataHolder.gpsColor))
        totalTimeTextview!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        durationTextview!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        icFlagVariantOutline!!.setColorFilter(Color.parseColor(AppDataHolder.gpsColor))
        totalDistanceTravelledTextview!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        lengthTextview!!.setTextColor(Color.parseColor(AppDataHolder.gpsColor))
        recenterButton!!.backgroundTintList = ColorStateList.valueOf(Color.parseColor(AppDataHolder.gpsColor))
        recenterButton!!.setBackgroundColor(Color.parseColor(AppDataHolder.gpsColor))
        val stars = rating!!.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.parseColor(AppDataHolder.gpsColor), PorterDuff.Mode.SRC_ATOP)
    }

    /**
     * Internet method to display all route POIs on the map (markers)
     */
    private fun displayPOIS(style : Style){

        var arrayPOIS = AppDataHolder.currentRoute!!.arrayPois
        if(AppDataHolder.currentRoute!!.hasStartingPoints){
            arrayPOIS = ArrayList<POI>(arrayPOIS.subList(AppDataHolder.currentStartingPoint?.posPOI!!, arrayPOIS.size))
        }

        POI.Type.values().forEach { type ->
            // Applying image for current type of POI
            if(!hasUserRequestedMockNavigation){
                if(MapUtils.shouldDisplayPOICateg(type))
                    style.addImage(type.id.toString(), _activity.resources.getDrawable(POI.Type.getDrawableID(type.id)))
            }else{
                style.addImage(type.id.toString(), _activity.resources.getDrawable(POI.Type.getDrawableID(type.id)))
            }


            // We get all pois matching current type from route
            var matchingPOIS = arrayPOIS.filter { poi ->
                poi.type == type
            }

            // Adding locations for current type of POI
            var poiFeatures = ArrayList<Feature>()
            matchingPOIS.forEach { poi ->
                var feature = Feature.fromGeometry(Point.fromLngLat(poi.arrayCoordinates.first().longitude, poi.arrayCoordinates.first().latitude))
                poiFeatures.add(feature)
                displayedFeatures.add(feature)

                if(hasUserRequestedMockNavigation){

                    // Adding source for current type of POI
                    if(style.getSource(RADIUS_MARKER_SOURCE + poi.id) == null)
                        style.addSource(
                                GeoJsonSource(RADIUS_MARKER_SOURCE + poi.id, TurfTransformation.circle(
                                        Point.fromLngLat(poi.mainCoordinates.longitude, poi.mainCoordinates.latitude), poi.radius.toDouble(), 64, "meters")))

                    val layer = FillLayer(RADIUS_MARKER_STYLE_LAYER + poi.id, RADIUS_MARKER_SOURCE + poi.id)
                    //val layer = CircleLayer(poi.id, RADIUS_MARKER_SOURCE + poi.id)
                    layer.withProperties(
                            PropertyFactory.fillColor("#000000"),
                            PropertyFactory.fillOpacity(0.5f)
                    )

                    //style.addLayerAbove(layer, MARKER_STYLE_LAYER+type.toString())
                    //style.addLayerBelow(layer, INVISIBLE_LAYER)
                    style.addLayerAt(layer, 50)
                }
            }

            // Adding source for current type of POI
            if(style.getSource(MARKER_SOURCE+type.toString()) == null)
                style.addSource(GeoJsonSource(MARKER_SOURCE+type.toString(), FeatureCollection.fromFeatures(poiFeatures)))


            // Applying layer for current type of POI
            if(style.getLayer(MARKER_STYLE_LAYER+type.toString()) == null)
                style.addLayerAbove(SymbolLayer(MARKER_STYLE_LAYER+type.toString(), MARKER_SOURCE+type.toString())
                        .withProperties(
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconIgnorePlacement(true),
                                PropertyFactory.iconImage(type.id.toString()),
                                PropertyFactory.iconSize(0.5f),
                                PropertyFactory.iconOffset(arrayOf(0f, -52f))
                        ), "route-line-layer")
            /**style.addLayerAt(SymbolLayer(MARKER_STYLE_LAYER+type.toString(), MARKER_SOURCE+type.toString())
            .withProperties(
            iconAllowOverlap(true),
            iconIgnorePlacement(true),
            iconImage(type.id.toString()),
            iconSize(0.5f),
            // Adjust the second number of the Float array based on the height of your marker image.
            // This is because the bottom of the marker should be anchored to the coordinate point, rather
            // than the middle of the marker being the anchor point on the map.
            iconOffset(arrayOf(0f, -52f))
            ), 60)**/


        }
    }

    /**
     * Add specific marker such as departure and arrival points
     */
    private fun displayMarkersOnMap(){


        var departureFeature = Feature.fromGeometry(Point.fromLngLat(AppDataHolder.currentStartingPoint?.long!!, AppDataHolder.currentStartingPoint?.lat!!))
        var arrivalFeature = Feature.fromGeometry(Point.fromLngLat(AppDataHolder.currentRoute?.arrayCoordinates?.last()?.longitude!!, AppDataHolder.currentRoute?.arrayCoordinates?.last()?.latitude!!))

        loadedStyle?.addSource(GeoJsonSource(DEPARTURE_MARKER_SOURCE, FeatureCollection.fromFeature(departureFeature)))
        loadedStyle?.addSource(GeoJsonSource(ARRIVAL_MARKER_SOURCE, FeatureCollection.fromFeature(arrivalFeature)))

        loadedStyle?.addLayerAbove(SymbolLayer(DEPARTURE_MARKER_SOURCE, DEPARTURE_MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconImage(DEPARTURE_MARKER_SOURCE),
                        PropertyFactory.iconSize(0.5f),
                        PropertyFactory.iconOffset(arrayOf(0f, -52f))
                ), "route-line-layer")

        loadedStyle?.addLayerAbove(SymbolLayer(ARRIVAL_MARKER_SOURCE, ARRIVAL_MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconImage(ARRIVAL_MARKER_SOURCE),
                        PropertyFactory.iconSize(0.5f),
                        PropertyFactory.iconOffset(arrayOf(0f, -52f))
                ), "route-line-layer")

    }

    /**
     * Callback when user clicks for a short time on the map
     */
    override fun onMapClick(point: LatLng): Boolean {
        var clickedFeatures : List<Feature>? = null
        var minDistance = 20000.0
        var closestPOI : POI? = null

        POI.Type.values().forEach { type ->
            var screenPoint = mapboxMap.projection.toScreenLocation(point)
            var rectF = RectF(screenPoint.x - 10, screenPoint.y - 10, screenPoint.x + 10, screenPoint.y + 10)
            var features = mapboxMap.queryRenderedFeatures(rectF, MARKER_STYLE_LAYER+type.toString())
            Log.d("onMapClick", "features for ${type.toString()} found ${features.size}")
            if(features.size > 0)
                clickedFeatures = features
        }

        if(clickedFeatures != null){ // In case we detected to have a click on an element of layer

            AppDataHolder.currentRoute!!.arrayPois.forEach { poi ->
                var distanceBetweenClickAndPois = TurfMeasurement.distance(MapUtils.convertLocationToPoint(poi.mainCoordinates), Point.fromLngLat(point.longitude, point.latitude))
                if(distanceBetweenClickAndPois < minDistance){
                    minDistance = distanceBetweenClickAndPois
                    closestPOI = poi
                }
            }
        }

        if(closestPOI != null){
            if(MapUtils.shouldDisplayPOI(closestPOI!!)){
                AppDataHolder.currentPOI = closestPOI!!
                //StatisticManager.poiClicked(closestPOI!!)
            }
            //moveCameraOnPOI(closestPOI!!.mainCoordinates)
            //moveCameraTo(closestPOI!!.mainCoordinates)
            currentDisplayedPOI = closestPOI
            return true
        }else{
            return false
        }
    }

    /**
     * Callback when user clicks for a long time on the map
     */
    override fun onMapLongClick(point: LatLng): Boolean {
        // Only reverse geocode while we are not in navigation
        if (mapState == MapState.NAVIGATION) {
            return false
        }

        // Fetch the route with this given point
        destination = Point.fromLngLat(point.longitude, point.latitude)
        //calculateRouteWith(destination, false)

        // Clear any existing markers and add new one
        navigationMap!!.clearMarkers()
        //navigationMap!!.addMarker(this, destination)

        // Update camera to new destination
        moveCameraToInclude(destination!!)
        //vibrate()
        return false
    }


    @SuppressLint("RestrictedApi")
    private fun showRecenterButton(show: Boolean){
        if(show) recenterButton!!.visibility = View.VISIBLE
        else recenterButton!!.visibility = View.GONE
    }

    /*
     * Navigation listeners
     */

    // Once navigation is finished
    override fun onNavigationFinished() {
        /*if(hasLeadStarted && !hasRunStarted){
            if(AppDataHolder.currentRoute?.hasStartingPoints!!){
                startRunNavigation(MapUtils.computeRouteCoordinatesBasedOnClosestPoint(AppDataHolder.currentStartingPoint?.getLocation()!!, AppDataHolder.currentRoute!!.arrayCoordinates!!.subList(AppDataHolder.currentStartingPoint?.pos!!, AppDataHolder.currentRoute!!.arrayCoordinates!!.size)))
            }else{
                startRunNavigation(MapUtils.computeRouteCoordinatesBasedOnClosestPoint(lastLocation!!, AppDataHolder.currentRoute!!.arrayCoordinates!!))
            }
        }else if(hasRunStarted){
            finishNavigation(true)
        }*/
        finishNavigation(true)
    }

    // While navigation is up and running
    override fun onNavigationRunning() {

    }

    // If we cancel navigation
    override fun onCancelNavigation() {

    }

    /**
     * Callback when route navigation is started, we can track progress of user on the route
     */
    override fun onProgressChange(location: Location, routeProgress: RouteProgress) {
        // Cache "snapped" Locations for re-route Directions API requests
        updateLocation(location)

        // Update InstructionView data from RouteProgress
        instructionView.updateDistanceWith(routeProgress)

        // Updating stats on the view
        updateStats(routeProgress.distanceRemaining())

        currentRunSession?.distance = routeProgress.distanceTraveled()

        // Detecting if user has arrived at the end of the route
        val ru = RouteUtils()
        //if(ru.isArrivalEvent(routeProgress) && hasRunStarted && AppDataHolder.currentRoute!!.isNavigationActivated!!){
        if(ru.isArrivalEvent(routeProgress)){
            finishNavigation(true)
        }
    }

    /**
     * Method to update stats, not all the parameters are necessary if they didn't change (enough or not at all)
     */
    private fun updateStats(distanceLeft: Double = 0.0, timer: Long = 0, speed: Float = 0f){
        if(AppDataHolder.currentRoute!!.isNavigationActivated!!){
            if(distanceLeft != 0.0)
                distanceLeftTextview!!.text = MetricsUtils.displayDistanceFormatted(distanceLeft)
            currentRunSession?.distance = AppDataHolder.currentRoute?.length?.toDouble()?.minus(distanceLeft)!!
        }else{
            var distanceBetweenLocations = TurfMeasurement.distance(Point.fromLngLat(lastLocation!!.longitude, lastLocation!!.latitude), Point.fromLngLat(previousLocation?.longitude!!, previousLocation?.latitude!!))
            if(distanceBetweenLocations < 1){
                Timber.d("distanceBetweenLocations : $distanceBetweenLocations")
                totalDistanceTravelled += distanceBetweenLocations
                Timber.d("totalDistanceTravelled : $totalDistanceTravelled")
                distanceLeftTextview!!.text = MetricsUtils.displayDistanceFormatted(AppDataHolder.currentRoute!!.length!! - totalDistanceTravelled)
            }
            if(hasRunStarted)
                checkIfUserHasArrived()
        }

        if(hasRunStarted && navigationState == NavigationState.IN_PROGRESS){
            if(timer != 0L)
                timerTextview!!.text = MetricsUtils.displayMillisecondsToSecondsFormatted(
                        NavigationUtils.currentStopWatchTimer!!.elapsedTime)
            totalTimeInMS += (timer-previousTime)
            previousTime = timer
            currentRunSession?.duration = NavigationUtils.currentStopWatchTimer!!.elapsedTime
        }
        if(speed != 0f){
            speedTextview!!.text = MetricsUtils.displaySpeedFormatted(speed)
            listSpeeds.add(speed)
            currentRunSession?.arraySpeed?.add(MetricsUtils.getHumanUnderstandableSpeed(speed).toDouble())
        }
        saveStatistics()
    }

    /**
     * Simple method to check if the user has arrived at the end of the route
     */
    private fun checkIfUserHasArrived(){
        Timber.d("distance left : ${AppDataHolder.currentRoute!!.length!! - totalDistanceTravelled}")
        // If user started manual navigation more than 5 minutes ago we check if he has closer than 20m from his starting point to check if he arrived to his point of departure
        if(NavigationUtils.currentStopWatchTimer!!.elapsedTime > 300000){
            if(TurfMeasurement.distance(Point.fromLngLat(lastLocation!!.longitude, lastLocation!!.latitude), Point.fromLngLat(startingLocation!!.longitude, startingLocation!!.latitude)) < 0.20){
                finishNavigation(true)
            }
        }
    }

    /**
     * Called every time mapbox detects a new instruction or event on the route
     */
    override fun onMilestoneEvent(routeProgress: RouteProgress, instruction: String, milestone: Milestone) {
        // Vocally playing the milestone
        playAnnouncement(milestone)

        // Update InstructionView banner instructions
        instructionView.updateBannerInstructionsWith(milestone)
    }

    /**
     * Callback method from mapbox navigation but also used manually in case of polyline navigation (mapbox off)
     */
    override fun userOffRoute(location: Location) {
        if(hasRunStarted && navigationState == NavigationState.IN_PROGRESS && AppDataHolder.currentRoute?.isNavigationActivated!!){
            // We check if user is further away than 20 meters from route
            if(MapUtils.getRouteDistanceFromUser(location, MapUtils.convertCoordinatesToPoints(AppDataHolder.currentRoute!!.arrayCoordinates!!)) > (25/1000)){
                if(lastTimeUserHasBeenOffRoute + 20000 < System.currentTimeMillis()){ // If we didn't warn user for 20 seconds
                    lastTimeUserHasBeenOffRoute = System.currentTimeMillis()
                    audioService.playTTS(LanguageResource.languagesResources.offRoute, true, true)
                }
            }
        }else if(!hasLeadBeenRequested){
            leadUserToRunRoute()
        }
    }

    /**
     * Called as soon as we get the first GPS signal / user location
     * Designed to setup the view / logic
     */
    internal fun checkFirstUpdate(location: Location) {
        if (lastLocation == null) {
            moveCameraTo(location)
            // Allow navigationMap clicks now that we have the current Location
            navigationMap!!.retrieveMap().addOnMapLongClickListener(this)
            //showSnackbar(GPS_FOUND_MESSAGE, BaseTransientBottomBar.LENGTH_LONG)
            setGPSSignal(true)
            //showNavigationHolderLayout(true)
            checkIfUserIsTooFarFromRoute(location)
        }
    }

    /**
     * Method made to check if the user is considered as too far from any point of the route or specific location
     */
    private fun checkIfUserIsTooFarFromRoute(location: Location?) : Boolean{
        var distanceFromRoute = MapUtils.getRouteDistanceFromUser(location!!, MapUtils.convertCoordinatesToPoints(AppDataHolder.currentRoute!!.arrayCoordinates!!))
        if(distanceFromRoute > 0.010){ // User has to be closer than 10 meters from the route to start it
            isUserTooFarFromRoute = true
            return true
        }else{
            isUserTooFarFromRoute = false
            return false
        }
    }

    /**
     * This methods manages the view (bottom banner with action buttons) based on the state of the navigation
     */
    private fun manageNavigationState(){
        when (navigationState) {
            NavigationState.WAITING_TO_START -> {
                navigationStateButton!!.text = _activity.getString(R.string.label_press_to_pause)
                showVisibilityToolbar(false)
                finishButton!!.visibility = View.VISIBLE
                resumeButton!!.visibility = View.GONE
                //navigationState = NavigationState.IN_PROGRESS
            }
            NavigationState.IN_PROGRESS -> {
                navigationStateButton!!.text = _activity.getString(R.string.label_press_to_resume)
                navigationStateButton!!.visibility = View.GONE
                finishButton!!.visibility = View.VISIBLE
                resumeButton!!.visibility = View.VISIBLE
                navigationState = NavigationState.PAUSED
                audioService.playTTS(LanguageResource.languagesResources.pauseNavigation, true, true)
                showVisibilityToolbar(false)
                NavigationUtils.currentStopWatchTimer?.pause()
            }
            NavigationState.PAUSED -> {
                navigationStateButton!!.text = _activity.getString(R.string.label_press_to_pause)
                navigationState = NavigationState.IN_PROGRESS
                navigationStateButton!!.visibility = View.VISIBLE
                finishButton!!.visibility = View.VISIBLE
                resumeButton!!.visibility = View.GONE
                if(NavigationUtils.currentStopWatchTimer != null && NavigationUtils.currentStopWatchTimer!!.isPaused)
                    NavigationUtils.currentStopWatchTimer?.resume()
                audioService.playTTS(LanguageResource.languagesResources.resumeNavigation, true, true)
            }
            NavigationState.FINISHED -> {
                navigationStateButton!!.text = _activity.getString(R.string.action_start_navigation)
                navigationState = NavigationState.WAITING_TO_START
                navigationStateButton!!.visibility = View.VISIBLE
                finishButton!!.visibility = View.GONE
                resumeButton!!.visibility = View.GONE
                statsLayout!!.visibility = View.GONE
            }
            else -> {
                navigationStateButton!!.text = _activity.getString(R.string.label_press_to_pause)
                navigationState = NavigationState.IN_PROGRESS
            }
        }
    }

    /**
     * Methods designed to show / hide components
     */

    private fun showStatsLayout(visible: Boolean){
        if(visible) statsLayout!!.visibility = View.VISIBLE
        else statsLayout!!.visibility = View.GONE
    }

    private fun manageVisibilityMenuLayout(visible: Boolean){
        if(visible) menuLayout!!.visibility = View.VISIBLE
        else menuLayout!!.visibility = View.GONE
    }

    /**
     * Method called every time user changes his location - called by MapBox
     */
    internal fun updateLocation(location: Location) {

        previousLocation = Location("provider")
        if(lastLocation != null){
            previousLocation!!.longitude = lastLocation!!.longitude
            previousLocation!!.latitude = lastLocation!!.latitude
        }

        lastLocation = location
        navigationMap!!.updateLocation(location)
        updateStats(0.0, System.currentTimeMillis(), location.speed)
        checkIfBannerIsDisplayed()

        if(hasUserRequestedNavigation || hasUserRequestedMockNavigation){
            if(hasRunStarted){
                checkIfUserIsNearPOI(location)
                if(!AppDataHolder.currentRoute!!.isNavigationActivated!!)
                    userOffRoute(location)
                else saveStatistics()
            }
            else{
                /*if(!checkIfUserIsTooFarFromRoute(location)){
                    navigation!!.stopNavigation()
                    //audioService.playTTS(LanguageResource.getLanguagesResources().routeReached, true, true)
                    if(AppDataHolder.currentRoute?.hasStartingPoints!!)
                        startRunNavigation(MapUtils.computeRouteCoordinatesBasedOnClosestPoint(AppDataHolder.currentStartingPoint?.getLocation()!!, AppDataHolder.currentRoute!!.arrayCoordinates!!.subList(AppDataHolder.currentStartingPoint?.pos!!, AppDataHolder.currentRoute!!.arrayCoordinates!!.size)))
                    else
                        startRunNavigation(MapUtils.computeRouteCoordinatesBasedOnClosestPoint(location!!, AppDataHolder.currentRoute!!.arrayCoordinates!!))
                }else{*/
                    checkIfUserIsNearPOI(location)
                    var computedClosestpoint = MapUtils.getClosestPointFromUser(location, MapUtils.convertCoordinatesToPoints(AppDataHolder.currentRoute!!.arrayCoordinates!!))
                    if(computedClosestpoint != closestRoutePoint){ // If our destination point to lead to run is different
                        // We need to check if there is an important difference for user between the two points
                        var distanceBetweenDestinations = TurfMeasurement.distance(computedClosestpoint, closestRoutePoint!!)
                        Log.d("updateLocation", "distance between destinations : $distanceBetweenDestinations")
                        Log.d("updateLocation", "distance between starting point and now : ${MapUtils.getDistanceFromLocation(lastLocation!!, startingLocation!!)}")
                        if(distanceBetweenDestinations > 0.150){
                            if(!hasLeadStarted)
                                leadUserToRunRoute()
                            closestRoutePoint = computedClosestpoint
                        }else if(MapUtils.getDistanceFromLocation(lastLocation!!, startingLocation!!) > 0.1){
                            leadUserToRunRoute()
                        }
                    }else if(MapUtils.getDistanceFromLocation(lastLocation!!, startingLocation!!) > 0.1){
                        leadUserToRunRoute()
                    }
                //}
            }
        }

    }

    /**
     * Method made to save current run session's statistics in Firebase, we trigger it quite often so we don't lose any user data in case of ANR / crash
     */
    private fun saveStatistics(){

        try{
            averageSpeed = listSpeeds.average().toFloat()
            totalTimeTravelled = NavigationUtils.currentStopWatchTimer!!.elapsedTime
        }catch(exception: Exception){
            Timber.e(exception.localizedMessage)
        }

        // We check first if we need to save stats regarding interval
        if(System.currentTimeMillis() - previousSaveTimeStamp > INTERVAL_BETWEEN_SAVES){
            if(currentRunSession != null){
                // NavigationStatisticManager.saveRunSession(currentRunSession!!)
                previousSaveTimeStamp = System.currentTimeMillis()
            }
        }
    }

    /**
     * Method to check if user is near POI
     */
    private fun checkIfUserIsNearPOI(userLocation: Location){
        isMapboxSpeakerMuted = checkIfMapboxSpeakIsMuted(userLocation, currentBlockerPOI) // Should we read POI ?
        AppDataHolder.currentRoute!!.arrayPois.forEach { poi ->
            MapUtils.getDistancesFromPOI(userLocation, poi).forEach { distance -> // Checking distance from every POI
                //Log.d("checkIfUserIsNearPOI", "distance from POI ${poi.name}: $distance <? ${((poi.radius).toFloat()/1000).toDouble()}")
                if(distance <= ((poi.radius).toFloat()/1000).toDouble()){ // If user is the POI radius
                    // If the poi is made to block mapbox intructions
                    if(poi.doesBlockMapboxSpeaker != null && poi.doesBlockMapboxSpeaker){
                        currentBlockerPOI = poi
                    }

                    if(!poi.isSeen){ // Checking that POI has never been played
                        if(poi.isMP3Activated != null && poi.isMP3Activated){ // Checking if we need to play vocal MP3 instead of text
                            audioService.playDescriptionMP3(poi, this)
                            poi.isSeen = true
                        }else{ // We read text
                            if(poi.type == POI.Type.DIRECTIONNAL_CLOCKWISE || poi.type == POI.Type.DIRECTIONNAL_COUNTERCLOCKWISE){ // Checking if this is a handmade navigation instruction
                                audioService.playTTS(SpeechAnnouncement.builder().announcement(poi.description).build(), true)
                                // We need to display custom instruction
                                if(AppDataHolder.currentRoute!!.isNavigationActivated!!){
                                    // TODO override mapbox instructions
                                }else{
                                    maneuverInstruction!!.text = poi.description
                                    maneuverImage!!.setImageDrawable(_activity.getDrawable(POI.Direction.getDrawableID(poi.idDirection)))
                                    Handler().postDelayed({
                                        _activity.runOnUiThread {
                                            maneuverInstruction!!.text = ""
                                            maneuverImage!!.setImageDrawable(null)
                                        }
                                    }, INSTRUCTION_DURATION_DISPLAY)
                                }
                            }
                            else{ // This is a "normal" POI
                                if(AppDataHolder.currentRoute!!.isSynthActivated!!){ // We check that route has synthetizer activated
                                    if(poi.isAutoDirectionAcivated)
                                        audioService.playTTS(audioService.generateAudioDescriptionForPOI(previousLocation, userLocation, poi), false, true)
                                    else{
                                        audioService.playTTS(poi.description, false, true)
                                    }

                                    val arguments: MutableMap<String, Any> = java.util.HashMap(1)
                                    arguments["poi"] = "{}"
                                    methodChannel!!.invokeMethod("onActivePOI", arguments)
                                }
                                else{ // We don't read any description, simply a generic message
                                    audioService.playTTS(LanguageResource.languagesResources.infoPOINearby, true, true)
                                }
                            }
                            poi.isSeen = true
                        }
                        // As we enter the radius, we check if we need to display the nearby POI
                        if(MapUtils.shouldDisplayPOI(poi)){
                            AppDataHolder.currentPOI = poi
                            if(poi.id != idGoodDealDisplayed && !isGoodDealDisplayed) displayBottomSheetFor(poi) //Or Banner
                            PhoneUtils.triggerVibration(500)
                        }
                        //StatisticManager.poiListened(poi)
                    }
                }else{
                    // In case we are no longer in the area of displayed poi -- we hide bottom sheet
                }
            }
        }
    }

    /**
     * Method made to check if MapBox navigation instructions should be read
     */
    private fun checkIfMapboxSpeakIsMuted(userLocation: Location, poi: POI?) : Boolean{
        if(poi == null) return false
        var distanceFromPOI = MapUtils.getDistanceFromPOI(userLocation, poi)
        return distanceFromPOI < ((poi.radius).toFloat()/1000).toDouble() && (poi.doesBlockMapboxSpeaker != null && poi.doesBlockMapboxSpeaker)
    }

    // TODO SINGLETON ?
    /**
     * Initialize Android TTS
     */
    private fun initializeSpeechPlayer() {
        val appLanguage = ApplicationRunner.appLanguage.getLocaleLanguage()
        val cache = Cache(
                File(_activity.application.cacheDir, COMPONENT_NAVIGATION_INSTRUCTION_CACHE),
                TEN_MEGABYTE_CACHE_SIZE
        )
        val voiceInstructionLoader = VoiceInstructionLoader(
                _activity.application,
                Mapbox.getAccessToken(), cache
        )
        val speechPlayerProvider = SpeechPlayerProvider(
                _activity.application, appLanguage, true,
                voiceInstructionLoader
        )
        speechPlayer = NavigationSpeechPlayer(speechPlayerProvider)
    }

    /**
     * Init mapview with correct language labels
     */
    private fun initLocalizationPlugin(mapView : MapView, mapboxMap: MapboxMap, style: Style){
        val localizationPlugin = LocalizationPlugin(mapView, mapboxMap, style)

        try {
            localizationPlugin.matchMapLanguageWithDeviceDefault()
        } catch (exception: RuntimeException) {
            Timber.d(exception.toString())
        }
    }

    /**
     * Managing our location engine for the navigation
     */
    @SuppressLint("MissingPermission")
    private fun initializeLocationEngine(route : DirectionsRoute? = null) {

        if(route != null){
            // Fake location engine to do a mock navigation for tests
            locationEngine = ReplayRouteLocationEngine()
            (locationEngine as ReplayRouteLocationEngine).assign(route)
            (locationEngine as ReplayRouteLocationEngine).updateSpeed(currentMockNavigationSpeed)
            val request = buildEngineRequest()
            //navigation!!.setLocationEngineRequest(request)
            locationEngine!!.requestLocationUpdates(request, callback, Looper.getMainLooper())
            navigation!!.locationEngine = (locationEngine as ReplayRouteLocationEngine)
        }else{ // Real location engine to perform real navigation
            locationEngine = LocationEngineProvider.getBestLocationEngine(_activity.applicationContext)
            val request = buildEngineRequest()
            //navigation!!.setLocationEngineRequest(request)
            locationEngine!!.requestLocationUpdates(request, callback, Looper.getMainLooper())
        }

        //showSnackbar(SEARCHING_FOR_GPS_MESSAGE, BaseTransientBottomBar.LENGTH_LONG)
        setGPSSignal(false)
    }

    /**
     * Methods to manage view depending on GPS state
     */
    private fun setGPSSignal(found: Boolean){
        if(!found){
            textLoader!!.text = _activity.getString(R.string.message_searching_for_gps)
            progressBarLoader!!.visibility = View.VISIBLE
            progressBarLoader!!.indeterminateDrawable.setColorFilter(_activity.resources.getColor(R.color.white),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            startNavigationHolder!!.visibility = View.GONE
        }
        else{
            Handler().postDelayed({
                _activity.runOnUiThread {
                    textLoader!!.text = _activity.getString(R.string.message_gps_found)
                    progressBarLoader!!.visibility = View.GONE
                }

                Handler().postDelayed({
                    showLoadingGPS(false)
                }, 3000)
            }, 3000)
        }
    }

    private fun showLoadingGPS(visible: Boolean, text: String = ""){
        if(visible){
            gpsLoaderHolder!!.visibility = View.VISIBLE
            startNavigationHolder!!.visibility = View.GONE
        }else{
            gpsLoaderHolder!!.visibility = View.GONE
            startNavigationHolder!!.visibility = View.VISIBLE
        }
        if(!text.equals("")){
            textLoader!!.text = text
        }
    }

    private fun initializeNavigation(mapboxMap: MapboxMap) {
        navigation = MapboxNavigation(_activity, accessToken)
        navigation!!.setLocationEngineRequest(buildEngineRequest())
        navigation!!.locationEngine = locationEngine!!
        navigation!!.cameraEngine = DynamicCamera(mapboxMap)
        navigation!!.addProgressChangeListener(this)
        navigation!!.addMilestoneEventListener(this)
        navigation!!.addOffRouteListener(this)
        navigationMap!!.addProgressChangeListener(navigation!!)
        navigationMap!!.updateTrafficVisibility(true)
        navigationMap!!.updateIncidentsVisibility(true)
    }

    private fun changeMockNavigationSpeed(speed : Int){
        (locationEngine as ReplayRouteLocationEngine).updateSpeed(1)
        navigation!!.locationEngine = (locationEngine as ReplayRouteLocationEngine)
    }

    private fun displayRouteOnMap(){

        var arrayFeatures = ArrayList<Feature>()
        var sortedArrayPoints: ArrayList<Point>

        if(!AppDataHolder.currentRoute?.hasStartingPoints!!){
            sortedArrayPoints = MapUtils.convertCoordinatesToPoints(AppDataHolder.currentRoute!!.previewCoordinates!!)
            if(AppDataHolder.currentRoute?.isLoop!!)
                sortedArrayPoints.add(MapUtils.convertLocationToPoint(AppDataHolder.currentRoute!!.previewCoordinates!!.first())) // We add first location in order to join first and last location graphically when drawing
        }else{
            sortedArrayPoints = MapUtils.convertCoordinatesToPoints((AppDataHolder.currentRoute!!.previewCoordinates!!.subList(AppDataHolder.currentStartingPoint?.pos!!, AppDataHolder.currentRoute!!.previewCoordinates!!.size)))
        }

        arrayFeatures.add(
                Feature.fromGeometry(
                        LineString.fromLngLats(sortedArrayPoints)
                ))

        loadedStyle!!.addSource(GeoJsonSource(INVISIBLE_SOURCE))
        loadedStyle!!.addLayer(LineLayer(INVISIBLE_LAYER, INVISIBLE_SOURCE))

        // We add polyline of route on map
        // Create the LineString from the list of coordinates and then make a GeoJSON
        // FeatureCollection so we can add the line to our map as a layer.
        try{
            loadedStyle!!.addSource(GeoJsonSource("line-source", FeatureCollection.fromFeatures(arrayFeatures)))
        }catch(exception: Exception){
            Log.e("crash", exception.localizedMessage)
        }
        // The layer properties for our line. This is where we make the line dotted, set the
        // color, etc.
        try{
            loadedStyle!!.addLayerBelow(
                    LineLayer("route-line-layer", "line-source").withProperties(
                            PropertyFactory.iconAllowOverlap(true),
                            PropertyFactory.iconIgnorePlacement(true),
                            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                            PropertyFactory.lineWidth(8f),
                            PropertyFactory.lineColor(Color.parseColor("#2196f3"))
                    ), INVISIBLE_LAYER)
        }catch(exception: Exception){
            Log.e("crash", exception.localizedMessage)
        }
    }

    private fun displayDebugRouteOnMap(arrayCoordinates : List<Location>){

        var arrayFeatures = ArrayList<Feature>()
        var sortedArrayPoints = MapUtils.convertCoordinatesToPoints(arrayCoordinates)
        sortedArrayPoints.add(MapUtils.convertLocationToPoint(arrayCoordinates.first())) // We add first location in order to join first and last location graphically when drawing
        arrayFeatures.add(
                Feature.fromGeometry(
                        LineString.fromLngLats(sortedArrayPoints)
                ))
        // We add polyline of route on map
        // Create the LineString from the list of coordinates and then make a GeoJSON
        // FeatureCollection so we can add the line to our map as a layer.
        try{
            loadedStyle!!.addSource(GeoJsonSource("line-source-debug", FeatureCollection.fromFeatures(arrayFeatures)))
        }catch(exception: Exception){
            Log.e("crash", exception.localizedMessage)
        }
        // The layer properties for our line. This is where we make the line dotted, set the
        // color, etc.
        try{
            loadedStyle!!.addLayer(
                    LineLayer("route-line-layer", "line-source-debug").withProperties(
                            PropertyFactory.iconAllowOverlap(true),
                            PropertyFactory.iconIgnorePlacement(true),
                            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                            PropertyFactory.lineWidth(3f),
                            PropertyFactory.lineColor(_activity.resources.getColor(R.color.mapbox_navigation_route_alternative_congestion_red))
                    ))
        }catch(exception: Exception){
            Log.e("crash", exception.localizedMessage)
        }
    }

    private fun playAnnouncement(milestone: Milestone) {
        if (milestone is VoiceInstructionMilestone) {
            val announcement = SpeechAnnouncement.builder()
                    .voiceInstructionMilestone(milestone)
                    .build()
            if(!isMapboxSpeakerMuted)
                AudioService.getInstance().playTTS(announcement, true)
            //speechPlayer!!.play(announcement)
        }
    }

    /**
     * CAMERA
     */
    private fun moveCameraTo(location: Location) {
        val cameraPosition = buildCameraPositionFrom(location, location.bearing.toDouble())
        navigationMap!!.retrieveMap().animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition), TWO_SECONDS_IN_MILLISECONDS
        )
    }

    private fun moveCameraOnPOI(location: Location){
        val cameraPosition = buildCameraPositionFrom(location, location.bearing.toDouble())
        navigationMap!!.retrieveMap().animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition), TWO_SECONDS_IN_MILLISECONDS
        )
    }

    private fun moveCameraToInclude(destination: Point) {
        val origin = LatLng(lastLocation!!)
        val bounds = LatLngBounds.Builder()
                .include(origin)
                .include(LatLng(destination.latitude(), destination.longitude()))
                .build()
        val resources = _activity.resources
        val routeCameraPadding = _activity.resources.getDimension(R.dimen.component_navigation_route_camera_padding).toInt()
        val padding = intArrayOf(
                routeCameraPadding,
                routeCameraPadding,
                routeCameraPadding,
                routeCameraPadding
        )
        val cameraPosition = navigationMap!!.retrieveMap().getCameraForLatLngBounds(bounds, padding)
        navigationMap!!.retrieveMap().animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition!!), TWO_SECONDS_IN_MILLISECONDS
        )
    }

    private fun moveCameraOverhead() {
        if (lastLocation == null) {
            return
        }
        val cameraPosition = buildCameraPositionFrom(lastLocation!!, DEFAULT_BEARING)
        navigationMap!!.retrieveMap().animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition), TWO_SECONDS_IN_MILLISECONDS
        )
    }

    private fun cameraOverheadUpdate(): CameraUpdate? {
        if (lastLocation == null) {
            return null
        }
        val cameraPosition = buildCameraPositionFrom(lastLocation!!, DEFAULT_BEARING)
        return CameraUpdateFactory.newCameraPosition(cameraPosition)
    }

    private fun buildCameraPositionFrom(location: Location, bearing: Double): CameraPosition {
        return CameraPosition.Builder()
                .zoom(DEFAULT_ZOOM)
                .target(LatLng(location.latitude, location.longitude))
                .bearing(bearing)
                .tilt(DEFAULT_TILT)
                .build()
    }

    private fun adjustMapPaddingForNavigation() {
        val resources = _activity.resources
        val mapViewHeight = mapView!!.height
        val bottomSheetHeight =
                resources.getDimension(R.dimen.component_navigation_bottomsheet_height).toInt()
        val topPadding = mapViewHeight - bottomSheetHeight * BOTTOMSHEET_PADDING_MULTIPLIER
        navigationMap!!.retrieveMap()
                .setPadding(ZERO_PADDING, topPadding, ZERO_PADDING, ZERO_PADDING)
    }

    private fun resetMapAfterNavigation() {
        navigationMap!!.removeRoute()
        navigationMap!!.clearMarkers()
        addEventToHistoryFile("cancel_navigation")
        navigation!!.stopNavigation()
        moveCameraOverhead()
    }

    private fun removeLocationEngineListener() {
        if (locationEngine != null) {
            locationEngine!!.removeLocationUpdates(callback)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addLocationEngineListener() {
        if (locationEngine != null) {
            val request = buildEngineRequest()
            locationEngine!!.requestLocationUpdates(request, callback, Looper.getMainLooper())
        }
    }


    /**private fun calculateRouteWith(destination: Point?, isOffRoute: Boolean) {
    val origin = Point.fromLngLat(lastLocation!!.longitude, lastLocation!!.latitude)
    val bearing = java.lang.Float.valueOf(lastLocation!!.bearing).toDouble()
    NavigationRoute.builder(this)
    .accessToken(Mapbox.getAccessToken()!!)
    .origin(origin, bearing, BEARING_TOLERANCE)
    .destination(destination!!)
    .build()
    .getRoute(object : Callback<DirectionsResponse> {
    override fun onResponse(
    call: Call<DirectionsResponse>,
    response: Response<DirectionsResponse>
    ) {
    handleRoute(response, isOffRoute)
    }

    override fun onFailure(call: Call<DirectionsResponse>, throwable: Throwable) {
    Timber.e(throwable)
    }
    })
    }**/

    /*private fun startRunNavigation(arrayCoordinates : List<Location>) {
        // Transition to navigation state
        mapState = MapState.NAVIGATION

        currentRunSession = RunSession.initSession(AppDataHolder.currentRoute!!, System.currentTimeMillis())

        Handler().postDelayed({
            hasRunStarted = true
        }, 2000)

        //cancelNavigationFab.show()

        // Show the InstructionView
        //TransitionManager.beginDelayedTransition(navigationLayout)
        instructionView.visibility = View.VISIBLE

        adjustMapPaddingForNavigation()
        // Updates camera with last location before starting navigating,
        // making sure the route information is updated
        // by the time the initial camera tracking animation is fired off
        // Alternatively, NavigationMapboxMap#startCamera could be used here,
        // centering the map camera to the beginning of the provided route
        navigationMap!!.resumeCamera(lastLocation!!)
        loadedStyle!!.removeSource("line-source")
        loadedStyle!!.removeLayer("route-line-layer")
        navigationMap!!.updateLocationLayerRenderMode(RenderMode.GPS)

        // We check if we need to activate mapbox or not
        if(AppDataHolder.currentRoute!!.isNavigationActivated!!){
            fetchRoute(arrayCoordinates)
            // Location updates will be received from ProgressChangeListener
            removeLocationEngineListener()
        }else{
            startManualNavigation()
        }

        navigationMap!!.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS)
        val cameraUpdate = cameraOverheadUpdate()
        if (cameraUpdate != null) {
            val navUpdate = NavigationCameraUpdate(cameraUpdate)
            navigationMap!!.retrieveCamera().update(navUpdate)
        }
    }*/

    private fun leadUserToRunRoute(){

        //mapState = MapState.INFO

        hasLeadBeenRequested = true

        /*if(AppDataHolder.currentRoute?.hasStartingPoints!!){
            closestRoutePoint = Point.fromLngLat(AppDataHolder.currentStartingPoint?.long!!, AppDataHolder.currentStartingPoint?.lat!!)
        }else{
            closestRoutePoint = MapUtils.getClosestPointFromUser(lastLocation!!, MapUtils.convertCoordinatesToPoints(AppDataHolder.currentRoute!!.arrayCoordinates!!))
        }*/

        closestRoutePoint = MapUtils.convertLocationToPoint(AppDataHolder.currentRoute!!.arrayPois[0].arrayCoordinates[0])


        startingLocation = lastLocation

        NavigationRoute.builder(_context)
                .accessToken(this.accessToken)
                .origin(MapUtils.convertLocationToPoint(lastLocation!!))
                .profile(AppDataHolder.currentRoute!!.methodLocomotion!!.getDirectionCriteria())
                .destination(closestRoutePoint!!)
                .language(ApplicationRunner.appLanguage.getLocale())
                .voiceUnits(DirectionsCriteria.METRIC)
                .build()
                .getRoute(object : Callback<DirectionsResponse> {
                    override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                        if(response.isSuccessful){
                            if(response.body()!!.routes().isNotEmpty()){
                                navigation!!.stopNavigation()
                                val computedRoute = response.body()?.routes()?.first()
                                calculatedRouteLeadUserToRun = computedRoute
                                navigation!!.startNavigation(computedRoute!!)
                                startNavTimestamp = System.currentTimeMillis()
                                initDistanceToTravel = computedRoute.distance()!!
                                hasLeadStarted = true
                                audioService.playTTS(LanguageResource.languagesResources.startNavigationToRoute, true, true)
                                showStatsLayout(true)
                                navigationState = NavigationState.IN_PROGRESS

                                // Transition to navigation state
                                mapState = MapState.NAVIGATION
                                navigationMap!!.updateLocationLayerRenderMode(RenderMode.GPS)

                                //cancelNavigationFab.show()

                                // Show the InstructionView
                                //TransitionManager.beginDelayedTransition(navigationLayout)
                                showLoadingGPS(false)
                                instructionView.visibility = View.VISIBLE

                                Handler().postDelayed({
                                    navigationMap!!.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS)
                                }, 1000)

                                //adjustMapPaddingForNavigation()

                                //navigationMap!!.resumeCamera(lastLocation!!)
                                //navigation!!.stopNavigation()
                            }else{
                                Toast.makeText(_context, _activity.getString(R.string.message_route_not_found), Toast.LENGTH_LONG).show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                        Log.e("onFailure", t.localizedMessage)
                        hasLeadBeenRequested = false
                    }
                })
        //navigation!!.startRunNavigation(route!!)

        // Location updates will be received from ProgressChangeListener
        removeLocationEngineListener()

        /**navigationMap!!.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS)
        val cameraUpdate = cameraOverheadUpdate()
        if (cameraUpdate != null) {
        val navUpdate = NavigationCameraUpdate(cameraUpdate)
        navigationMap!!.retrieveCamera().update(navUpdate)
        }**/
    }

    private fun startManualNavigation(){
        navigationMap!!.removeRoute()
        navigationMap!!.clearMarkers()
        navigation!!.stopNavigation()
        hasRunStarted = true
        loadedStyle!!.addLayer(
                LineLayer("route-line-layer", "line-source").withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                        PropertyFactory.lineWidth(10f),
                        PropertyFactory.lineColor(_activity.resources.getColor(R.color.colorPrimary))
                ))
        enableLocationComponent(loadedStyle!!)
        startNavTimestamp = System.currentTimeMillis()
        showStatsLayout(true)
        navigationState = NavigationState.IN_PROGRESS
        addLocationEngineListener()
        NavigationUtils.startTimer()
        initDistanceToTravel = AppDataHolder.currentRoute!!.length!!.toDouble()
        startingLocation = lastLocation!!
        initInstructionView()
        showCustomInstructionView(true)
    }

    private fun fetchRoute(arrayCoordinates : List<Location>){

        // Algorithm to keep as much coordinates as possible from route coordinates
        // The more we keep, the better will be the computed route
        var simplifiedArrayCoordinates = arrayCoordinates
        var coordinatesSize = arrayCoordinates.size
        var dividingFactor = 1
        for(i in dividingFactor..20){
            if(coordinatesSize/i < 100){
                dividingFactor = i
                break
            }
        }
        // We check initial number of coordinates - mapbox accepts maximum 100 coordinates
        // If we have less than 100 coordinates, we keep them all
        if(arrayCoordinates.size > 100){
            simplifiedArrayCoordinates = arrayCoordinates.filterIndexed { index, _ ->
                index % dividingFactor == 0
            }
        }

        /**
         * We start a matching request to mapbox api in order to get a route that suits perfectly existing routes
         */
        if(AppConfiguration.Navigation.debugMode) displayDebugRouteOnMap(simplifiedArrayCoordinates)
        MapboxMapMatching.builder()
                .accessToken(this.accessToken)
                .coordinates(MapUtils.convertCoordinatesToPoints(simplifiedArrayCoordinates))
                .steps(true)
                .waypointIndices(0, MapUtils.convertCoordinatesToPoints(simplifiedArrayCoordinates).size-1)
                .voiceInstructions(true)
                .bannerInstructions(true)
                .language(ApplicationRunner.appLanguage.getLocale())
                .profile(AppDataHolder.currentRoute!!.methodLocomotion!!.getDirectionCriteria())
                .voiceUnits(DirectionsCriteria.METRIC) // TODO based on language
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .build()
                .enqueueCall(object : Callback<MapMatchingResponse> {
                    override fun onResponse(call: Call<MapMatchingResponse>, response: Response<MapMatchingResponse>) {
                        if (response.isSuccessful) {
                            try{
                                val route = response.body()!!.matchings()!![0].toDirectionRoute()

                                if(hasUserRequestedMockNavigation){
                                    initializeLocationEngine(route)
                                }

                                calculatedRouteRun = route
                                //handleRoute(route, false)
                                audioService.playTTS(LanguageResource.languagesResources.runStarted, true, true)
                                navigationMap!!.drawRoute(route)
                                navigation!!.startNavigation(route)
                                navigation!!.locationEngine = locationEngine!!
                                startNavTimestamp = System.currentTimeMillis()
                                currentRunSession?.totalDistance = route.distance()!!
                                NavigationUtils.startTimer()
                                showStatsLayout(true)
                                navigationState = NavigationState.IN_PROGRESS
                            }catch (exception: Exception){
                                Toast.makeText(_context, "Cannot generate route : $exception", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<MapMatchingResponse>, throwable: Throwable) {

                    }
                })
    }

    private fun addEventToHistoryFile(type: String) {
        val secondsFromEpoch = Date().time / 1000.0
        navigation!!.addHistoryEvent(type, secondsFromEpoch.toString())
    }

    private fun buildEngineRequest(): LocationEngineRequest {
        return LocationEngineRequest.Builder(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .build()
    }

    /**
     * Calls bottom sheet to be displayed
     */
    private fun displayBottomSheetFor(poi: POI){
        AppDataHolder.currentPOI = poi
        bindBottomSheetWith(poi)
        recenterButton!!.hide()
        bottomSheetPoiBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        if(audioService.isPOIBeingRead(poi)){
            playPoiButton!!.visibility = View.GONE
            mp3Fillprogresslayout!!.visibility = View.VISIBLE
            stopPoiButton!!.visibility = View .VISIBLE
            Handler().postDelayed({
                mp3FillprogresslayoutStatic!!.visibility = View.GONE
            }, 750)
        }else{
            playPoiButton!!.visibility = View.VISIBLE
            stopPoiButton!!.visibility = View.GONE
            mp3Fillprogresslayout!!.visibility = View.GONE
            mp3FillprogresslayoutStatic!!.visibility = View.VISIBLE
        }

        var durationBeforeClosing = 30000L
        if(poi.isMP3Activated!!){
            durationBeforeClosing = 60000L
        }

        Handler().postDelayed({
            _activity.runOnUiThread {
                bottomSheetPoiBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }, durationBeforeClosing)
    }

    private fun displayBannerFor(poi: POI){
        val slide_down = AnimationUtils.loadAnimation(_activity.applicationContext, R.anim.slide_down_gooddeal)
        val display = _activity.windowManager.defaultDisplay
        val size = android.graphics.Point()
        display.getSize(size)
        val heightDisplay: Float = size.y - coordinatorBannerPoi!!.y
        _activity.runOnUiThread {
            coordinatorBannerPoi!!.visibility = View.VISIBLE
            coordinatorBannerPoi!!.alpha = 0.0f
            if(poi.imageURL != null){
                Glide.with(_activity.applicationContext).load(poi.imageURL).into(imageBannerPoi!!)
                (imageBannerPoi!!).visibility = View.VISIBLE
            }else{
                (imageBannerPoi!!).visibility = View.GONE
            }
            (titleBannerPoi!!).text = poi.name
            (descriptionBannerPoi!!).text = _activity.getString(R.string.text_know_more_about_poi)
            (linkGooddeal!!).setOnClickListener {
                IntentUtils.startWebIntent(_context, poi.urlWebView, false)
                //StatisticManager.poiClicked(poi)
            }
        }
        coordinatorBannerPoi!!.animate()
                .translationY(_activity.resources.getDimension(R.dimen.instruction_layout_height))
                .alpha(1.0f)
                .setListener(null)
        timeStampStartGooddealAnimation = System.currentTimeMillis()
        isGoodDealDisplayed = true
        idGoodDealDisplayed = poi.id
        Timber.i("Showing gooddeal")
    }

    private fun checkIfBannerIsDisplayed(){
        // Checking if a gooddeal is displayed - if yes and gooddeal timer is over, we hide it
        if (System.currentTimeMillis() - timeStampStartGooddealAnimation > LENGTH_ANIMATION_GOODDEAL_MS && isGoodDealDisplayed) { // Hiding a gooddeal on screen
            _activity.runOnUiThread {
                coordinatorBannerPoi!!.animate()
                        .translationY(0f)
                        .alpha(0.0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                coordinatorBannerPoi!!.setVisibility(View.INVISIBLE)
                            }
                        })
                isGoodDealDisplayed = false
                Timber.i("Hiding gooddeal")
            }
        }
    }

    /**
     * Fills bottom sheet with data from POI
     */
    /**
     * Fills bottom sheet with data from POI
     */
    private fun bindBottomSheetWith(poi: POI){
        if(poi.arrayMP3.isEmpty())
            poiPlayer!!.visibility = View.GONE
        else
            poiPlayer!!.visibility = View.VISIBLE
        titlePoiTextview!!.text = poi.name
        if(poi.imageURL != null && poi.imageURL != ""){
            imagePoiImageview!!.visibility = View.VISIBLE
            Glide.with(_context.applicationContext)
                    .load(poi.imageURL)
                    .placeholder(R.drawable.placeholder_image_loading)
                    .into(imagePoiImageview!!)
        }else{
            imagePoiImageview!!.visibility = View.GONE
        }

        /**
         * Depending on kind of POI we have to display different content
         */
        if(poi.type == POI.Type.CHALLENGE)
            descriptionPoiTextview!!.visibility = View.GONE
        else{
            descriptionPoiTextview!!.visibility = View.VISIBLE
            descriptionPoiTextview!!.text = poi.description
        }
    }

    /**
     * Methods to detect state of reading of MP3 files on POIs
     */

    override fun onListeningProgress(initialDuration: Long, currentProgress: Int) {
        Log.d("onProgress", ((currentProgress*100)/initialDuration.toInt()).toString())
        mp3Fillprogresslayout!!.setProgress((currentProgress*100)/initialDuration.toInt())
        mp3TimeTextview!!.text = MetricsUtils.displayMillisecondsToSecondsFormatted(currentProgress)
    }

    override fun initMP3Duration(duration: Long) {
        playPoiButton!!.visibility = View.GONE
        stopPoiButton!!.visibility = View.VISIBLE
    }

    override fun onListeningFinished() {
        mp3Fillprogresslayout!!.setProgress(0)
        mp3TimeTextview!!.text = MetricsUtils.displayMillisecondsToSecondsFormatted(0)
        playPoiButton!!.visibility = View.VISIBLE
        stopPoiButton!!.visibility = View.GONE
    }

    /*
     * LocationEngine callback
     */
    private class ComponentActivityLocationCallback internal constructor(activity: MileNavigationToPOIEngine) : LocationEngineCallback<LocationEngineResult> {

        private val activityWeakReference: WeakReference<MileNavigationToPOIEngine>

        init {
            this.activityWeakReference = WeakReference(activity)
        }

        override fun onSuccess(result: LocationEngineResult) {
            val activity = activityWeakReference.get()
            if (activity != null) {
                val location = result.lastLocation ?: return
                activity.checkFirstUpdate(location)
                activity.updateLocation(location)
                Log.d("location", location.toString())
            }
        }

        override fun onFailure(exception: Exception) {
            Timber.e(exception)
        }
    }

    /**
     * Method to finish current navigation properly
     */
    private fun finishNavigation(completed: Boolean){
        if(!hasRouteFinished){
            hasRouteFinished = true
            // Hide the InstructionView
            instructionView.visibility = View.INVISIBLE
            mapState = MapState.INFO

            if(hasRunStarted)
                saveStatistics()
            if((NavigationUtils.currentStopWatchTimer != null) && (NavigationUtils.currentStopWatchTimer!!.isPaused || NavigationUtils.currentStopWatchTimer!!.isStarted))
                NavigationUtils.stopTimer()
            resetMapAfterNavigation()
            bottomSheetEndBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetEndBehavior?.isHideable = false
            bindResumeBottomSheet()

            if(completed)
                audioService.playTTS(LanguageResource.languagesResources.congratulationsFinishRoute, true, true)
            else
                audioService.playTTS(LanguageResource.languagesResources.finishNavigation, true, true)
        }
    }

    /**
     * Binding final bottom sheet when route is finished
     */
    private fun bindResumeBottomSheet(){
        //val stars = rating.progressDrawable as LayerDrawable
        //stars.getDrawable(2).setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)

        totalDistanceTravelledTextview!!.text = MetricsUtils.displayDistanceFormatted(totalDistanceTravelled)
        totalTimeTextview!!.text = MetricsUtils.displayMillisecondsToSecondsFormatted(totalTimeTravelled)
        averageSpeedTextview!!.text = MetricsUtils.displaySpeedFormatted(averageSpeed)

        rating!!.onRatingBarChangeListener = object: RatingBar.OnRatingBarChangeListener{
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                hasUserChangedRating = true
            }
        }
    }

    open fun init() {
        when (activityState!!.get()) {
            STOPPED -> {
                mapView.onCreate(null)
                mapView.onStart()
                mapView.onResume()
                mapView.onPause()
                mapView.onStop()
            }
            PAUSED -> {
                mapView.onCreate(null)
                mapView.onStart()
                mapView.onResume()
                mapView.onPause()
            }
            RESUMED -> {
                mapView.onCreate(null)
                mapView.onStart()
                mapView.onResume()
            }
            STARTED -> {
                mapView.onCreate(null)
                mapView.onStart()
            }
            CREATED -> {
                mapView.onCreate(null)
            }
            DESTROYED -> {
                mapView.onDestroy()
            }
            else -> throw IllegalArgumentException(
                    "Cannot interpret " + activityState!!.get() + " as an activity state");
        }
        mapView.getMapAsync(this)
    }

    override fun onActivityPaused(activity: Activity?) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return
        }
        mapView.onPause()
    }

    override fun onActivityResumed(activity: Activity?) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return
        }
        mapView.onResume()
    }

    override fun onActivityStarted(activity: Activity?) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return
        }
        mapView.onStart()
        if (navigationMap != null) {
            navigationMap!!.onStart()
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return
        }
        mapView.onDestroy()
        audioService.stopAudioService(false)

        // Ensure proper shutdown of the SpeechPlayer
        if (speechPlayer != null) {
            speechPlayer!!.onDestroy()
        }

        // Prevent leaks
        removeLocationEngineListener()

        (navigation!!.cameraEngine as DynamicCamera).clearMap()

        // MapboxNavigation will shutdown the LocationEngine
        navigation!!.onDestroy()
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return
        }
        mapView.onSaveInstanceState(outState!!)
    }

    override fun onActivityStopped(activity: Activity?) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return
        }
        mapView.onStop()
        if (navigationMap != null) {
            navigationMap!!.onStop()
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return
        }
        mapView.onCreate(savedInstanceState)
    }

    /*
     * Activity lifecycle methods
     */
}