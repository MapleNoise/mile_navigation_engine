package com.mile.mile_navigation_engine.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDexApplication;

import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;
import com.mapbox.mapboxsdk.Mapbox;
import com.mile.mile_navigation_engine.model.LanguageCode;
import com.mile.mile_navigation_engine.model.MetricsCode;
import com.mile.mile_navigation_engine.utils.ApplicationPreferences;
import com.mile.mile_navigation_engine.interfaces.OnUserLocationFetched;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Class that stores global application state
 */
public class ApplicationRunner extends MultiDexApplication {

    private static final String CLOUD_FUNCTIONS_API_URL = "https://us-central1-mythalassa-1bc58.cloudfunctions.net/";
    private static final String TAG = ApplicationRunner.class.getSimpleName();

    public static ApplicationRunner instance;

    private ApplicationPreferences appPrefs;

    private Retrofit cloudFunctionsRetrofitHandler;

    private Retrofit decathlonRetrofitHandler;

    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private GoogleApiClient mGoogleApiClient;

    private OnUserLocationFetched userLocationListener;

    public static LanguageCode appLanguage = LanguageCode.FR;
    public static MetricsCode appSpeedMetrics = MetricsCode.SPEED_KMH;
    public static MetricsCode appDistanceMetrics = MetricsCode.METRIC;

    public DrawableCrossFadeFactory drawableFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        drawableFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        appLanguage = LanguageCode.Companion.getLanguageCodeFromLocale(Locale.getDefault());
        appSpeedMetrics = MetricsCode.Companion.getSpeedMetricFromLanguageCode(appLanguage);
        appDistanceMetrics = MetricsCode.Companion.getDistanceMetricFromLanguageCode(appLanguage);


        // Setting up logging
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return String.format("C:%s:%s",
                        super.createStackElementTag(element), element.getLineNumber());
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Init preferences
        this.appPrefs = new ApplicationPreferences(this);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        // Init cloudFunctionsRetrofitHandler
        this.cloudFunctionsRetrofitHandler = new Retrofit.Builder()
                .baseUrl(CLOUD_FUNCTIONS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Log.d(TAG, "got user location" + location.toString());
                        ApplicationRunner.instance.setUserLocation(location);
                        ApplicationRunner.instance.getUserLocationListener().onSuccess(location);
                        stopLocationUpdates();
                    }
                }
            }
        };

        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000);

        userLocationListener = new OnUserLocationFetched() {
            @Override
            public void onSuccess(Location location) {
                ApplicationRunner.instance.setUserLocation(location);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }

    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public Location retrieveCurrentUserLocation() {
        final Location userLocation = null;
            /**ApplicationRunner.instance.getFusedLocationClient().getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                try{
                                    Log.i(TAG, "last user location is : " + location.getLatitude() + " " + location.getLongitude());
                                    ApplicationRunner.instance.setUserLocation(location);
                                    Log.i(TAG, "Getting nearest city");
                                    userLocation.set(location);
                                }catch(Exception e){
                                    Log.e(TAG, e.getLocalizedMessage());
                                }

                            }
                        }
                    });**/

            /**ApplicationRunner.instance.setLocationRequest(LocationRequest.create());
            ApplicationRunner.instance.getLocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            ApplicationRunner.instance.getLocationRequest().setInterval(1000); // Ask location every second
            ApplicationRunner.instance.setLocationCallback(new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            Log.d(TAG, "got user location" + location.toString());
                            ApplicationRunner.instance.setUserLocation(location);
                            ApplicationRunner.instance.getFusedLocationClient().removeLocationUpdates(ApplicationRunner.instance.getLocationCallback()); // We stop location loop request
                        }
                    }
                }
            });**/

            startLocationUpdates();



        return userLocation;
    }



    /**
     * GETTERS AND SETTERS
     */

    public ApplicationPreferences getAppPrefs() {
        return appPrefs;
    }

    public Context getAppContext(){
        return this.getApplicationContext();
    }

    public Retrofit getCloudFunctionsRetrofitHandler() {
        return cloudFunctionsRetrofitHandler;
    }

    public void setCloudFunctionsRetrofitHandler(Retrofit cloudFunctionsRetrofitHandler) {
        this.cloudFunctionsRetrofitHandler = cloudFunctionsRetrofitHandler;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public FusedLocationProviderClient getFusedLocationClient() {
        return fusedLocationClient;
    }

    public void setFusedLocationClient(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;
    }

    public LocationRequest getLocationRequest() {
        return locationRequest;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }

    public LocationCallback getLocationCallback() {
        return locationCallback;
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public OnUserLocationFetched getUserLocationListener() {
        return userLocationListener;
    }

    public void setUserLocationListener(OnUserLocationFetched userLocationListener) {
        this.userLocationListener = userLocationListener;
    }

    public Retrofit getDecathlonRetrofitHandler() {
        return decathlonRetrofitHandler;
    }

    public void setDecathlonRetrofitHandler(Retrofit decathlonRetrofitHandler) {
        this.decathlonRetrofitHandler = decathlonRetrofitHandler;
    }

    public LanguageCode getAppLanguage() {
        return LanguageCode.FR;
    }

    public void setAppLanguage(LanguageCode appLanguage) {
        this.appLanguage = appLanguage;
    }

    public DrawableCrossFadeFactory getDrawableFactory() {
        return drawableFactory;
    }

    public void setDrawableFactory(DrawableCrossFadeFactory drawableFactory) {
        this.drawableFactory = drawableFactory;
    }

    public static String getCloudFunctionsApiUrl() {
        return CLOUD_FUNCTIONS_API_URL;
    }
}