package com.mile.mile_navigation_engine.utils.Alerts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;


public class AlertCreator {

    static public AlertDialog.Builder createAlert(String title, String message, String buttonLabel, Context context) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton(buttonLabel, null);
        return adb;
    }

    static public Toast createToast(String title, Context context) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, title, duration);
        return toast;
    }

    static public AlertDialog.Builder createAlertWithActions(Context context, String title, String message, String positiveButtonLabel, DialogInterface.OnClickListener  positiveListener, String negativeButtonLabel, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton(positiveButtonLabel,positiveListener);
        adb.setNegativeButton(negativeButtonLabel,negativeListener);
        return adb;
    }
}
