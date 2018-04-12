package com.lesincs.simpleread.custom

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.chad.library.adapter.base.animation.BaseAnimation

/**
 * Created by Administrator on 2018/3/30.
 */
class CustomScaleAnimation : BaseAnimation {
    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f))
    }
}