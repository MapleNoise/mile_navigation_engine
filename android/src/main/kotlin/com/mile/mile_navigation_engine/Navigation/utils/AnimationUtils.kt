package com.mile.mile_navigation_engine.utils

import android.widget.TextView
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


class AnimationUtils {

    companion object{

        fun changeTextWithFadeInOut(textview: TextView, text: String){
            var animIn = AlphaAnimation(0.0f, 1.0f)
            animIn.setDuration(250)

            var animOut = AlphaAnimation(1.0f, 0.0f)
            animOut.setDuration(250)

            animOut.setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                    // TODO
                }

                override fun onAnimationEnd(p0: Animation?) {
                    textview.text = text
                    textview.startAnimation(animIn)
                }

                override fun onAnimationStart(p0: Animation?) {
                    // TODO
                }

            })

            textview.startAnimation(animOut)
        }
    }

}