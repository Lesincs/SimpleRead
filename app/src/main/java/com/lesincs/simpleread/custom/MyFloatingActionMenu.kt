package com.lesincs.simpleread.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.github.clans.fab.FloatingActionMenu

/**
 * Created by Administrator on 2018/1/25.
 */
class MyFloatingActionMenu : FloatingActionMenu {

    private val DEFAULT_ANIMATION_DURATION = 500L
    private var isInHideAnimation = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    fun customHide(duration: Long = DEFAULT_ANIMATION_DURATION) {
        if (visibility == View.INVISIBLE)
            return
        if (isInHideAnimation)
            return
        if (isOpened) {
            close(true)
            return
        }
        val fabMenuY = height.toFloat() + (layoutParams as RelativeLayout.LayoutParams).bottomMargin
        isInHideAnimation =true
        animate().
                translationY(fabMenuY)
                .setInterpolator(FastOutSlowInInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        visibility = View.INVISIBLE
                        isInHideAnimation = false
                    }
                })
                .setDuration(duration).start()

    }

    fun customShow(duration: Long = DEFAULT_ANIMATION_DURATION) {
        if (visibility == View.VISIBLE)
            return
        visibility = View.VISIBLE
        animate().
                translationY(0F)
                .setInterpolator(FastOutSlowInInterpolator())
                .setListener(object :AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                    }
                })
                .setDuration(duration).start()
    }
}
