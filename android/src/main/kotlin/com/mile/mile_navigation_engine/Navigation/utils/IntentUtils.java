package com.mile.mile_navigation_engine.Navigation.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.mile.mile_navigation_engine.R;

import timber.log.Timber;

public class IntentUtils {

    public static String FACEBOOK_URL = "https://www.facebook.com/RunninCity";
    public static String FACEBOOK_PAGE_ID = "RunninCity";

    public static void startInfoAppIntent(Context context){
        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + "com.lmsporttourism.runnincity"));
            context.startActivity(intent);

        } catch ( ActivityNotFoundException e ) {
            //e.printStackTrace();

            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);

        }
    }

    static public void startInstagramIntent(final Activity currentActivity) {
        Uri uri = Uri.parse("https://www.instagram.com/runnincity/?hl=fr");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");

        try {
            currentActivity.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            currentActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/runnincity/?hl=fr")));
        }
    }

    static public void startCallIntent(final Context context, String numberToCall){
        String uri = "tel:" + numberToCall.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        try{
            context.startActivity(intent);
        }catch(Exception e){
            Timber.e(e.getLocalizedMessage());
        }
    }

    static public void startWebIntent(final Context context, String urlWebsite, Boolean external){
        if(urlWebsite != null){
            String url = urlWebsite.trim();
            if(external){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                try{
                    context.startActivity(intent);
                }catch(Exception e){
                    Timber.e(e.getLocalizedMessage());
                }
            }else{
                //ActivityWebView.Companion.navigate((Activity) context, url, false);
            }
        }else{
            Toast.makeText(context, context.getString(R.string.no_url_error), Toast.LENGTH_SHORT).show();
        }


    }
}
