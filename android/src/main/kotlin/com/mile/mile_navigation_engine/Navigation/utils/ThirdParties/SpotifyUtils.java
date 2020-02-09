package com.mile.mile_navigation_engine.utils.ThirdParties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class SpotifyUtils {

    //   Set up your Spotify credentials :
    private static final String CLIENT_ID = "8e7d23b6bd0245719a28d5a65f41482c";
    private static final String REDIRECT_URI = "https://www.runnincity.fr/callback"; // my-awesome-app-login://callback //"http://com.yourdomain.yourapp/callback";
   // private SpotifyAppRemote mSpotifyAppRemote;
    private static final int CODE_SPOTIFY = 105;
    private static final int CODE_SPOTIFY_OPEN = 106;
    private String spotify_link = "";//"https://open.spotify.com/playlist/4mOHEDksK8LKs3PPRs22wy"; //FB link
    private String spotify_link_application = "";

   public static boolean spotifyInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean isSpotifyInstalled = false;
        try {
            pm.getPackageInfo("com.spotify.music", 0);
            return isSpotifyInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            return isSpotifyInstalled = false;
        }

    }


    public static void launchSpotifyIntent(Context context, String spotify_link) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(spotify_link));

        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.getApplicationContext().getPackageName()));

        context.startActivity(intent);
    }

    public void installSpotifyFromMarket( Activity activity) {


        final String appPackageName = "com.spotify.music";
        final String referrer = "adjust_campaign=PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";

        try {
            Uri uri = Uri.parse("market://details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build();
           activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, uri), CODE_SPOTIFY);

        } catch (android.content.ActivityNotFoundException ignored) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build();
            activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, uri), CODE_SPOTIFY);
        }
    }

    public static String parseSpotifyLink(String link) {
        String spotify = "spotify";
        try {
            URL url = new URL(link);
           final String file = url.getFile();
            String spotify_link_application = file.replace("/", ":");
            spotify_link_application = spotify + spotify_link_application;
            return spotify_link_application;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
