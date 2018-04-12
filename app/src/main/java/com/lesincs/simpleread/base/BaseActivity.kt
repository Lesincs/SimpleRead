package com.lesincs.simpleread.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lesincs.simpleread.ui.activity.MainActivity
import com.lesincs.simpleread.R
import com.lesincs.simpleread.util.PrefUtil

/**
 * Created by Administrator on 2018/1/17.
 */
abstract class BaseActivity : AppCompatActivity() {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getThemeId()) //在setContentView之前设置主题
        setContentView(getLayoutId())
        afterOnCreate(savedInstanceState)
    }

    abstract fun afterOnCreate(savedInstanceState: Bundle?) //子类只需重写这个方法即可

    /*换主题的逻辑,因为MainActivity背景不能设置为透明需要单独配置主题*/
    private fun getThemeId(): Int {
        if (this is MainActivity) {
            return when (PrefUtil.getTheme()) {
                0 -> R.style.DefaultThemeMain
                1 -> R.style.AliveRedThemeMain
                2 -> R.style.BiliPinkThemeMain
                3 -> R.style.CoolApkGreenThemeMain
                4 -> R.style.DuckGreenThemeMain
                5 -> R.style.SkyBlueThemeMain
                6 -> R.style.OrangeThemeMain
                else -> R.style.DefaultThemeMain
            }

        } else {
            return when (PrefUtil.getTheme()) {
                0 -> R.style.DefaultTheme
                1 -> R.style.AliveRedTheme
                2 -> R.style.BiliPinkTheme
                3 -> R.style.CoolApkGreenTheme
                4 -> R.style.DuckGreenTheme
                5 -> R.style.SkyBlueTheme
                6 -> R.style.OrangeTheme
                else -> R.style.DefaultTheme
            }
        }
    }

    abstract fun getLayoutId(): Int


    /*带动画的结束方法*/
    fun finishSelf() {
        when (PrefUtil.getActivityAnim()) {
            PrefUtil.ACTIVITY_ANIM_FADE_IN_FADE_OUT ->
            {
                finish()
                overridePendingTransition(0, android.R.anim.fade_out)
            }
            PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_NORMAL ->{
                finish()
                overridePendingTransition(0, R.anim.slide_out_right_normal)
            }
            PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_FAST -> {
                finish()
                overridePendingTransition(0, R.anim.slide_out_right_fast)
            }
            PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_SLOW -> {
                finish()
                overridePendingTransition(0, R.anim.slide_out_right_slow)
            }
            else ->finish()
        }
    }

}