package com.lesincs.simpleread.custom

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.chad.library.adapter.base.animation.BaseAnimation

/**
 * Created by Administrator on 2018/3/31.
 */
class CustomSlideInLeftAnimation : BaseAnimation {
    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width / 2f, 0f))
    }
}