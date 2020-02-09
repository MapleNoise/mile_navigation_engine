package com.mile.mile_navigation_engine.utils.Animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Karl on 06/09/2016.
 */

public class AnimationHandler {

    static public void fadeIn(final View theView, long durationAnimation, final Float fromAlpha , final Float toAlpha){

        if (!fromAlpha.equals(toAlpha)) {
            Animation fadeIn = new AlphaAnimation(fromAlpha, toAlpha);
         //   fadeIn.setInterpolator(new AccelerateInterpolator());
            fadeIn.setDuration(durationAnimation);
            // fadeIn.setStartOffset(0);

            AnimationSet animation = new AnimationSet(false);
            animation.addAnimation(fadeIn);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    theView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                  //  theView.setAlpha(toAlpha);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            theView.startAnimation(animation);
            theView.setAlpha(toAlpha);
        }

    }

    static public void fadeOut(final View theView, long durationAnimation, final Float fromAlpha){

        Animation fadeOut = new AlphaAnimation(fromAlpha, 0f);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(durationAnimation);

        AnimationSet animationFadeOut = new AnimationSet(false);
        animationFadeOut.addAnimation(fadeOut);
        animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                theView.setAlpha(0f);
                theView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        theView.setAnimation(animationFadeOut);
        theView.startAnimation(animationFadeOut);

    }


}
