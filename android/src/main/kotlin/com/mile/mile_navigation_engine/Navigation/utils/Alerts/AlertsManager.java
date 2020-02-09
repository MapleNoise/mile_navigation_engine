package com.mile.mile_navigation_engine.utils.Alerts;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.mile.mile_navigation_engine.fragments.Dialogs.*;
import com.mile.mile_navigation_engine.interfaces.OnAlertClosed;
import com.mile.mile_navigation_engine.interfaces.OnAlertWithTextClosed;
import com.mile.mile_navigation_engine.interfaces.OnDialogActionClicked;

import java.util.ArrayList;

/**
 * Created by Corentin on 18/04/2019.
 */


/**
 * The purpose of this class if to handle calls of dialogs that are going to be reused through the app
 * Prevents from getting boilerplate and identical code in different classes
 */
public class AlertsManager {

    private static final String TAG = AlertsManager.class.getSimpleName();

    /**
     * Show an advice to user if he doesnt have internet access when starting navigation activity
     * @param activity
     * @param listener
     */
    public static void showNavigationNoInternetDialog(Activity activity, OnAlertClosed listener) {
        FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
        DialogNavigationErrorNoInternet dialogLocationPermissionFragment = DialogNavigationErrorNoInternet.newInstance(listener);
        dialogLocationPermissionFragment.show(fm, "fragment_ask_permission_location");
    }

}
