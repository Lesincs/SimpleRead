package com.lesincs.simpleread.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseActivity
import com.lesincs.simpleread.ui.fragment.DAILY_ARTICLE_PAGE_TYPE
import com.lesincs.simpleread.ui.fragment.DailyArticlePageType
import com.lesincs.simpleread.ui.fragment.JDNewsListFrag
import com.lesincs.simpleread.ui.fragment.ZHNewsListFrag
import com.lesincs.simpleread.util.CalenderUtil
import com.lesincs.simpleread.util.CircleRevealAnimUtil
import com.lesincs.simpleread.util.PrefUtil
import kotlinx.android.synthetic.main.activity_main_new.*
import kotlinx.android.synthetic.main.floating_action_bar_menu.*
import java.util.*

class MainActivity : BaseActivity() {

    private var isFirstStart = true
    private var lastOnBackPressTime: Long = 0
    private lateinit var zhNewsPrevFrag: ZHNewsListFrag
    private lateinit var JDNewsListFrag: JDNewsListFrag
    private lateinit var currentFrag: Fragment

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        initView(savedInstanceState)
        initFabListener()
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
    }

    private fun initView(savedInstanceState: Bundle?) {
        //初始化spinner
        spinner.setItems(getString(R.string.toolbar_zh_frag), getString(R.string.toolbar_jd_frag))

        //初始化fragment
        zhNewsPrevFrag = ZHNewsListFrag()
        JDNewsListFrag = JDNewsListFrag()

        //设置spinner切换的监听
        spinner.setOnItemSelectedListener { view, position, id, item ->
            when (position) {
                0 -> {
                    showFabMenu()
                    switchFrag(zhNewsPrevFrag)
                }
                1 -> {
                    hideFabMenu()
                    switchFrag(JDNewsListFrag)
                }
            }
        }

        //根据上次退出的界面加载fragment
        if (PrefUtil.getCurrentItem() == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, zhNewsPrevFrag).commit()
            currentFrag = zhNewsPrevFrag
            spinner.selectedIndex = 0
            showFabMenu()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, JDNewsListFrag).commit()
            currentFrag = JDNewsListFrag
            spinner.selectedIndex = 1
            if (isFirstStart) {
                isFirstStart = false
                fabMenu.postDelayed({ hideFabMenu() }, 300)
            } else {
                hideFabMenu()
            }
        }

        //初始化菜单栏
        ivMenu.setOnClickListener {
            val popMenu = PopupMenu(this, ivMenu, Gravity.CENTER)
            popMenu.inflate(R.menu.menu_activity_main)
            popMenu.setOnMenuItemClickListener {
                when (it!!.itemId) {
                    R.id.action_about -> {
                        InfoActivity.startSelf(this)
                    }
                    R.id.action_change_theme -> {
                        val themes = arrayOf(R.string.default_color,
                                R.string.alive_red,
                                R.string.bili_pink,
                                R.string.cool_apk_green,
                                R.string.duck_green,
                                R.string.sky_blue,
                                R.string.orange).map { getText(it) }.toTypedArray()

                        AlertDialog.Builder(this)
                                .setSingleChoiceItems(themes, PrefUtil.getTheme(), { d, p ->
                                    if (p != PrefUtil.getTheme()) {
                                        PrefUtil.saveTheme(p)
                                        onChangeTheme(p)
                                    }
                                    d.dismiss()
                                }).show()
                    }
                    R.id.action_collection -> {
                        CollectionActivity.startSelf(this)
                    }
                    R.id.action_setting -> {
                        SettingActivity.startSelf(this)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popMenu.show()
        }
    }

    private fun switchFrag(willShowFrag: Fragment) {
        if (currentFrag == willShowFrag)
            return

        if (!willShowFrag.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, willShowFrag).commit()
        }

        supportFragmentManager.beginTransaction().hide(currentFrag).show(willShowFrag).commit()
        currentFrag = willShowFrag
    }

    private fun initFabListener() {
        fabRandomPage.setOnClickListener { zhNewsPrevFrag.presenter.onRandomPage() }

        fabDailyArticle.setOnClickListener {
            CircleRevealAnimUtil.startActivity(this, Intent(this, DailyArticleActivity::class.java).putExtra(DAILY_ARTICLE_PAGE_TYPE, DailyArticlePageType.TODAYPAGE.toString()), fabDailyArticle, getAccentColor(), 618)
        }

        fabDailyArticleRandom.setOnClickListener {
            CircleRevealAnimUtil.startActivity(this, Intent(this, DailyArticleActivity::class.java).putExtra(DAILY_ARTICLE_PAGE_TYPE, DailyArticlePageType.RANDOMPAGE.toString()), fabDailyArticleRandom, getAccentColor(), 618)
        }

        fabChooseDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, R.style.MyDatePickerDialogTheme, { _, year, monthOfYear, dayOfMonth ->
                val sDate = CalenderUtil.getTheDayAfter(year, monthOfYear, dayOfMonth)
                zhNewsPrevFrag.presenter.onSpecificDate(sDate)
                PrefUtil.saveYear(year)
                PrefUtil.saveMonth(monthOfYear)
                PrefUtil.saveDay(dayOfMonth)
            }, PrefUtil.getYear(), PrefUtil.getMonth(), PrefUtil.getDay())
            datePickerDialog.datePicker.maxDate = CalenderUtil.getZHDatePickerMaxTime()
            datePickerDialog.datePicker.minDate = CalenderUtil.getZHDatePickerMinTime()
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "RESET", { _, _ ->
            })
            datePickerDialog.create()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setOnClickListener {
                val year = CalenderUtil.nowCalendar.get(Calendar.YEAR)
                val month = CalenderUtil.nowCalendar.get(Calendar.MONTH)
                val dayOfMonth = CalenderUtil.nowCalendar.get(Calendar.DAY_OF_MONTH)
                datePickerDialog.datePicker.updateDate(year, month, dayOfMonth)
                datePickerDialog.show()
                PrefUtil.saveYear(year)
                PrefUtil.saveMonth(month)
                PrefUtil.saveDay(dayOfMonth)
            }
            datePickerDialog.show()
        }
    }

    private fun onChangeTheme(p: Int) {
        val themeId = when (p) {
            0 -> R.style.DefaultThemeMain
            1 -> R.style.AliveRedThemeMain
            2 -> R.style.BiliPinkThemeMain
            3 -> R.style.CoolApkGreenThemeMain
            4 -> R.style.DuckGreenThemeMain
            5 -> R.style.SkyBlueThemeMain
            6 -> R.style.OrangeThemeMain
            else -> R.style.DefaultThemeMain
        }

        setTheme(themeId)

        val colorPrimaryTV = TypedValue()
        val colorPrimaryDarkTV = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, colorPrimaryTV, true)
        theme.resolveAttribute(R.attr.colorPrimaryDark, colorPrimaryDarkTV, true)
        val colorPrimary = resources.getColor(colorPrimaryTV.resourceId)
        val colorPrimaryDark = resources.getColor(colorPrimaryDarkTV.resourceId)
        rlToolbar.setBackgroundColor(colorPrimary)
        spinner.setBackgroundColor(colorPrimary)
        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        window.statusBarColor = colorPrimaryDark
        hideFabMenu()
        zhNewsPrevFrag.changeSwipeColor(colorPrimary)
        JDNewsListFrag.changeSwipeColor(colorPrimary)
        fabMenu.postDelayed({
            fabMenu.menuButtonColorNormal = colorPrimary
            fabMenu.menuButtonColorPressed = colorPrimary
            fabChooseDate.colorNormal = colorPrimary
            fabChooseDate.colorPressed = colorPrimary
            fabDailyArticle.colorNormal = colorPrimary
            fabDailyArticle.colorPressed = colorPrimary
            fabDailyArticleRandom.colorNormal = colorPrimary
            fabDailyArticleRandom.colorPressed = colorPrimary
            fabRandomPage.colorNormal = colorPrimary
            fabRandomPage.colorPressed = colorPrimary
            showFabMenu()
        }, 900)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main_new
    }

    private fun hideFabMenu() {
        if (fabMenu.isOpened) {
            fabMenu.close(true)
            fabMenu.postDelayed({ fabMenu.customHide() }, 350)
        }
        fabMenu.customHide()
    }

    private fun showFabMenu() {
        if (spinner.selectedIndex == 1)
            return
        fabMenu.customShow()
    }

    override fun onBackPressed() {
        if (fabMenu.visibility == View.VISIBLE && fabMenu.isOpened) {
            fabMenu.close(true)
            return
        }

        val currentOnBackPressTime = System.currentTimeMillis()

        if (currentOnBackPressTime - lastOnBackPressTime < 2000) {
            finish()
        } else {
            Snackbar.make(spinner, R.string.press_again_to_exit, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.exit, { finish() })
                    .show()
            lastOnBackPressTime = currentOnBackPressTime
        }
    }

    private fun getAccentColor(): Int {
        val colorAccentTV = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, colorAccentTV, true)
        return colorAccentTV.resourceId
    }

    override fun onDestroy() {
        PrefUtil.saveCurrentItem(spinner.selectedIndex)
        super.onDestroy()
    }

}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.snack(msg: String, view: View) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

}

