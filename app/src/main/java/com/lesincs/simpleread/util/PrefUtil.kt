package com.lesincs.simpleread.util

import android.preference.PreferenceManager
import com.lesincs.simpleread.base.App
import com.lesincs.simpleread.custom.EBgColor
import java.util.*

/**
 * Created by Administrator on 2017/12/27.
 */
class PrefUtil {

    companion object {

        private val YEAR = "YEAR"
        private val MONTH = "MONTH"
        private val DAY = "DAY"
        private val IS_OPEN_LINK_WITH_INNER_BROWSER = "OPEN_LINK_WITH_INNER_BROWSER"
        private val SEEK_BAR_PROGRESS = "seek_bar_progress"
        private val defaultPref = PreferenceManager.getDefaultSharedPreferences(App.sContext)
        private val BG_COLOR = "BG_COLOR"
        private val THEME = "THEME"
        private val VIEWPAGER_CURRENT_ITEM = "VIEWPAGER_CURRENT_ITEM"

        private val ACTIVITY_ANIM = "ACTIVITY_ANIM"
        val ACTIVITY_ANIM_FADE_IN_FADE_OUT = "ACTIVITY_ANIM_FADE_IN_FADE_OUT"
        val ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_SLOW = "ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_SLOW"
        val ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_NORMAL = "ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_NORMAL"
        val ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_FAST = "ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_FAST"


        private val ITEM_ANIM = "ITEM_ANIM"
        val ITEM_ANIM_NO_ANIM = "ITEM_ANIM_NO_ANIM"
        val ITEM_ANIM_SLIDE_IN_LEFT = "ITEM_ANIM_SLIDE_IN_LEFT"
        val ITEM_ANIM_SLIDE_IN_RIGHT = "ITEM_ANIM_SLIDE_IN_RIGHT"
        val ITEM_ANIM_SLIDE_IN_BOTTOM = "ITEM_ANIM_SLIDE_IN_BOTTOM"
        val ITEM_ANIM_ALPHA = "ITEM_ANIM_ALPHA"
        val ITEM_ANIM_SCALE = "ITEM_ANIM_SCALE"

        val ITEM_ANIM_IS_FIRST_ONLY = "ITEM_ANIM_IS_FIRST_ONLY"

        fun saveYear(year: Int) {
            defaultPref.edit().putInt(YEAR, year).apply()
        }

        fun saveMonth(year: Int) {
            defaultPref.edit().putInt(MONTH, year).apply()
        }

        fun saveDay(year: Int) {
            defaultPref.edit().putInt(DAY, year).apply()
        }

        fun getYear(): Int {
            return defaultPref.getInt(YEAR, CalenderUtil.nowCalendar.get(Calendar.YEAR))
        }

        fun getMonth(): Int {
            return defaultPref.getInt(MONTH, CalenderUtil.nowCalendar.get(Calendar.MONTH))
        }

        fun getDay(): Int {
            return defaultPref.getInt(DAY, CalenderUtil.nowCalendar.get(Calendar.DAY_OF_MONTH))
        }


        fun getIsOpenLinkWithLocalBrowser(): Boolean {
            return defaultPref.getBoolean(IS_OPEN_LINK_WITH_INNER_BROWSER, false)
        }


        fun saveSeekBarProgress(progress: Int) {
            defaultPref.edit().putInt(SEEK_BAR_PROGRESS, progress).apply()
        }

        fun getSeekBarProgress(): Int {
            return defaultPref.getInt(SEEK_BAR_PROGRESS, 3)
        }

        fun getLastBgColor(): EBgColor {
            val sBgColor = defaultPref.getString(BG_COLOR, EBgColor.WHITE.toString())
            return EBgColor.valueOf(sBgColor)
        }

        fun saveBgColor(bgColor: EBgColor) {
            defaultPref.edit().putString(BG_COLOR, bgColor.toString()).apply()
        }

        fun getTheme(): Int {
            return defaultPref.getInt(THEME, 0)
        }

        fun saveTheme(iTheme: Int) {
            defaultPref.edit().putInt(THEME, iTheme).apply()
        }

        fun saveCurrentItem(item: Int) {
            defaultPref.edit().putInt(VIEWPAGER_CURRENT_ITEM, item).apply()
        }

        fun getCurrentItem(): Int {
            return defaultPref.getInt(VIEWPAGER_CURRENT_ITEM, 0)
        }

        fun getActivityAnim(): String {
            return defaultPref.getString(ACTIVITY_ANIM, ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_NORMAL)
        }

        fun getItemAnim(): String {
            return defaultPref.getString(ITEM_ANIM, ITEM_ANIM_SCALE)
        }

        fun itemAnimIsFirsOnly(): Boolean {
            return defaultPref.getBoolean(ITEM_ANIM_IS_FIRST_ONLY, false)
        }
    }
}