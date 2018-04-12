package com.lesincs.simpleread.ui.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseActivity
import com.lesincs.simpleread.ui.adapter.FragAdapter
import com.lesincs.simpleread.ui.fragment.DAILY_ARTICLE_PAGE_TYPE
import com.lesincs.simpleread.ui.fragment.DailyArticlePageType
import com.lesincs.simpleread.ui.fragment.JDNewsListFrag
import com.lesincs.simpleread.ui.fragment.ZHNewsListFrag
import com.lesincs.simpleread.util.CalenderUtil
import com.lesincs.simpleread.util.CircleRevealAnimUtil
import com.lesincs.simpleread.util.PrefUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.floating_action_bar_menu.*
import java.util.*

class MainActivity : BaseActivity() {

    private var isFirstStart = true
    private var lastOnBackPressTime: Long = 0
    private lateinit var zhNewsPrevFrag: ZHNewsListFrag
    private lateinit var JDNewsListFrag: JDNewsListFrag
    private val ZH_NEWS_PREV_FRAG_KEY = "ZH_NEWS_PREV_FRAG_KEY"
    private val JD_NEWS_PREV_FRAG_KEY = "JD_NEWS_PREV_FRAG_KEY"

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        initView(savedInstanceState)
        initFabListener()
    }

    private fun initView(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.toolbar_zh_frag)

        if (savedInstanceState != null) {
            zhNewsPrevFrag = supportFragmentManager.getFragment(savedInstanceState, ZH_NEWS_PREV_FRAG_KEY) as ZHNewsListFrag
            JDNewsListFrag = supportFragmentManager.getFragment(savedInstanceState, JD_NEWS_PREV_FRAG_KEY) as JDNewsListFrag
        } else {
            zhNewsPrevFrag = ZHNewsListFrag()
            JDNewsListFrag = JDNewsListFrag()
        }

        viewPagerAM.offscreenPageLimit = 1
        viewPagerAM.adapter = FragAdapter(supportFragmentManager, listOf(zhNewsPrevFrag, JDNewsListFrag))
        viewPagerAM.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        toolbar.setTitle(R.string.toolbar_zh_frag)
                        showFabMenu()
                    }
                    1 -> {
                        toolbar.setTitle(R.string.toolbar_jd_frag)
                        if (isFirstStart) {
                            isFirstStart = false
                            fabMenu.postDelayed({ hideFabMenu() }, 300)
                        } else {
                            hideFabMenu()
                        }
                    }
                }
            }
        })

        val currentItem = PrefUtil.getCurrentItem()
        if (currentItem == 1) {
            viewPagerAM.currentItem = currentItem
        }
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
        toolbar.setBackgroundColor(colorPrimary)
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
        return R.layout.activity_main
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, ZH_NEWS_PREV_FRAG_KEY, zhNewsPrevFrag)
        supportFragmentManager.putFragment(outState, JD_NEWS_PREV_FRAG_KEY, JDNewsListFrag)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
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
        return super.onOptionsItemSelected(item)
    }

    private fun hideFabMenu() {
        if (fabMenu.isOpened) {
            fabMenu.close(true)
            fabMenu.postDelayed({ fabMenu.customHide() }, 350)
        }
        fabMenu.customHide()
    }

    private fun showFabMenu() {
        if (viewPagerAM.currentItem == 1)
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
            finishSelf()
        } else {
            Snackbar.make(viewPagerAM, R.string.press_again_to_exit, Snackbar.LENGTH_SHORT)
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
        PrefUtil.saveCurrentItem(viewPagerAM.currentItem)
        super.onDestroy()
    }


}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.snack(msg: String, view: View) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

}

