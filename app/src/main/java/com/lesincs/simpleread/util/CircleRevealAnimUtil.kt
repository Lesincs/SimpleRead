package com.lesincs.simpleread.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.ImageView


/**
 * Created by Administrator on 2017/12/27.
 */
class CircleRevealAnimUtil {

    companion object {


        private val PERFECT_MILLS: Long = 618
        private val MINI_RADIUS = 0

        /**
         * 向四周伸张，直到完成显示。
         */

        private fun show(myView: View, startRadius: Float, durationMills: Long) {

            val cx = (myView.left + myView.right) / 2
            val cy = (myView.top + myView.bottom) / 2

            val w = myView.width
            val h = myView.height

            // 勾股定理 & 进一法
            val finalRadius = Math.sqrt((w * w + h * h).toDouble()).toInt() + 1

            val anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, startRadius, finalRadius.toFloat())
            myView.visibility = View.VISIBLE
            anim.duration = durationMills
            anim.start()
        }

        /**
         * 由满向中间收缩，直到隐藏。
         */

        private fun hide(myView: View, endRadius: Float, durationMills: Long) {


            val cx = (myView.left + myView.right) / 2
            val cy = (myView.top + myView.bottom) / 2
            val w = myView.width
            val h = myView.height

            // 勾股定理 & 进一法
            val initialRadius = Math.sqrt((w * w + h * h).toDouble()).toInt() + 1

            val anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius.toFloat(), endRadius)
            anim.duration = durationMills
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    myView.visibility = View.INVISIBLE
                }
            })

            anim.start()
        }

        /**
         * 从指定View开始向四周伸张(伸张颜色或图片为colorOrImageRes), 然后进入另一个Activity,
         * 返回至 @thisActivity 后显示收缩动画。
         */

        private fun startActivityForResult(
                thisActivity: Activity, intent: Intent, requestCode: Int?, bundle: Bundle?,
                triggerView: View, colorOrImageRes: Int, _durationMills: Long) {
            var durationMills = _durationMills


            val location = IntArray(2)
            triggerView.getLocationInWindow(location)
            val cx = location[0] + triggerView.width / 2
            val cy = location[1] + triggerView.height / 2
            val view = ImageView(thisActivity)
            view.scaleType = ImageView.ScaleType.CENTER_CROP
            view.setImageResource(colorOrImageRes)
            val decorView = thisActivity.window.decorView as ViewGroup
            val w = decorView.width
            val h = decorView.height
            decorView.addView(view, w, h)

            // 计算中心点至view边界的最大距离
            val maxW = Math.max(cx, w - cx)
            val maxH = Math.max(cy, h - cy)
            val finalRadius = Math.sqrt((maxW * maxW + maxH * maxH).toDouble()).toInt() + 1
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
            val maxRadius = Math.sqrt((w * w + h * h).toDouble()).toInt() + 1
            // 若使用默认时长，则需要根据水波扩散的距离来计算实际时间
            if (durationMills == PERFECT_MILLS) {
                // 算出实际边距与最大边距的比率
                val rate = 1.0 * finalRadius / maxRadius
                // 水波扩散的距离与扩散时间成正比
                durationMills = (PERFECT_MILLS * rate).toLong()
            }
            val finalDuration = durationMills
            anim.duration = finalDuration
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    if (requestCode == null)
                        thisActivity.startActivity(intent)
                    else if (bundle == null)
                        thisActivity.startActivityForResult(intent, requestCode)
                    else
                        thisActivity.startActivityForResult(intent, requestCode, bundle)

                    // 默认渐隐过渡动画.
                    thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    // 默认显示返回至当前Activity的动画.
                    triggerView.postDelayed({
                        val anim_ = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius.toFloat(), 0f)
                        anim_.duration = finalDuration
                        anim_.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                try {
                                    decorView.removeView(view)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        })
                        anim_.start()
                    }, 1000)

                }
            })
            anim.start()
        }


        /*下面的方法全是重载，用简化上面方法的构建*/


        fun startActivityForResult(
                thisActivity: Activity, intent: Intent, requestCode: Int?, triggerView: View, colorOrImageRes: Int) {
            startActivityForResult(thisActivity, intent, requestCode, null, triggerView, colorOrImageRes, PERFECT_MILLS)
        }

        fun startActivity(
                thisActivity: Activity, intent: Intent, triggerView: View, colorOrImageRes: Int, durationMills: Long) {
            startActivityForResult(thisActivity, intent, null, null, triggerView, colorOrImageRes, durationMills)
        }

        fun startActivity(
                thisActivity: Activity, intent: Intent, triggerView: View, colorOrImageRes: Int) {
            startActivity(thisActivity, intent, triggerView, colorOrImageRes, PERFECT_MILLS)
        }

        fun startActivity(thisActivity: Activity, targetClass: Class<*>, triggerView: View, colorOrImageRes: Int) {
            startActivity(thisActivity, Intent(thisActivity, targetClass), triggerView, colorOrImageRes, PERFECT_MILLS)
        }

        fun show(myView: View) {
            show(myView, MINI_RADIUS.toFloat(), PERFECT_MILLS)
        }

        fun hide(myView: View) {
            hide(myView, MINI_RADIUS.toFloat(), PERFECT_MILLS)
        }
    }
}