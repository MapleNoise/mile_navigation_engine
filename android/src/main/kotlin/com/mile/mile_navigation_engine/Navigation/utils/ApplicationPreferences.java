package com.mile.mile_navigation_engine.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class that holds application preferences.
 */
public class ApplicationPreferences {

    public static final String DOWNLOAD_STEP_INDEX_PREF_KEY = "downloadStepIndex";

    public static final String DOWNLOAD_QUEUE_PREF_KEY = "downloadQueue";

    public static final String CURRENT_VERSION_CODE = "currentVersionCode";

    public static final String MAP_RESOURCES_UPDATE_NEEDED = "mapResourcesUpdateNeeded";

    // Allergies
    public static final String USER_ALLERGIES = "userAllergies";
    public static final String USER_ALLERGIES_ARRAY_KEY = "userAllergiesArray";

    // Authentication
    public static final String AUTH_STATE_PREFERENCE = "AuthStatePreference";
    public static final String AUTH_STATE = "AUTH_STATE";
    public static final String USED_INTENT = "USED_INTENT";
    public static final String CURRENT_PROVIDER = "current_provider";

    /**
     * preference arrayName
     */
    public static final String PREFS_NAME = "demoAppPrefs";

    /**
     * numberUse for modifying values in a SharedPreferences prefs
     */
    private SharedPreferences.Editor prefsEditor;

    /**
     * reference to preference
     */
    private SharedPreferences prefs;

    /**
     * the context
     */
    private Context context;

    public String customProvider;

    public ApplicationPreferences(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        customProvider = context.getSharedPreferences(ApplicationPreferences.AUTH_STATE_PREFERENCE, Context.MODE_PRIVATE).getString(ApplicationPreferences.CURRENT_PROVIDER, "");
        prefsEditor = prefs.edit();
    }

    public int getIntPreference(String key) {
        return prefs.getInt(key, 0);
    }

    public String getStringPreference(String key) {
        return prefs.getString(key, "");
    }

    public boolean getBooleanPreference(String key) {
        return prefs.getBoolean(key, false);
    }

    public void saveDownloadStepPreference(int downloadStepIndex) {
        prefsEditor.putInt(DOWNLOAD_STEP_INDEX_PREF_KEY, downloadStepIndex);
        prefsEditor.commit();
    }

    public void setCurrentVersionCode(int versionCode) {
        prefsEditor.putInt(CURRENT_VERSION_CODE, versionCode);
        prefsEditor.commit();
    }


    public void saveStringPreference(String key, String value){
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void saveBooleanPreference(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public String getCustomProvider() {
        return context.getSharedPreferences(ApplicationPreferences.AUTH_STATE_PREFERENCE, Context.MODE_PRIVATE).getString(ApplicationPreferences.CURRENT_PROVIDER, "");
    }

    public void setCustomProvider(String customProvider) {
        this.customProvider = customProvider;
    }
}
