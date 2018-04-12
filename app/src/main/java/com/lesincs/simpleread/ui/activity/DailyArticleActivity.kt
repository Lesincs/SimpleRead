package com.lesincs.simpleread.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.widget.SeekBar
import android.widget.Toast
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.R
import com.lesincs.simpleread.custom.EBgColor
import com.lesincs.simpleread.mvp.dailyarticlemvp.DailyArticleContract
import com.lesincs.simpleread.mvp.dailyarticlemvp.DailyArticlePresenter
import com.lesincs.simpleread.ui.fragment.DAILY_ARTICLE_PAGE_TYPE
import com.lesincs.simpleread.util.PrefUtil
import kotlinx.android.synthetic.main.activity_daily_article.*
import kotlinx.android.synthetic.main.layout_daily_article_setting.*

class DailyArticleActivity : AppCompatActivity(), DailyArticleContract.View {

    lateinit var mWebView: WebView
    private val mPresenter = DailyArticlePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_daily_article)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        webViewADA.settings.setSupportZoom(true)
        mWebView = webViewADA
        mWebView.settings.textZoom = getRoom(PrefUtil.getSeekBarProgress())

        mPresenter.onStart(intent.getStringExtra(DAILY_ARTICLE_PAGE_TYPE))

        webViewADA.setOnLongClickListener {
            val btDialog = BottomSheetDialog(this)
            btDialog.setContentView(R.layout.layout_daily_article_setting)
            btDialog.create()

            with(btDialog) {
                when (PrefUtil.getLastBgColor()) {
                    EBgColor.WHITE -> rg.check(R.id.rbWhite)
                    EBgColor.BROWN -> rg.check(R.id.rbBrown)
                    EBgColor.BLACK -> rg.check(R.id.rbBlack)
                }
                rg.setOnCheckedChangeListener({ _, id ->
                    when (id) {
                        R.id.rbWhite -> {
                            PrefUtil.saveBgColor(EBgColor.WHITE)
                            mPresenter.onBgChange()
                        }
                        R.id.rbBrown -> {
                            PrefUtil.saveBgColor(EBgColor.BROWN)
                            mPresenter.onBgChange()
                        }
                        R.id.rbBlack -> {
                            PrefUtil.saveBgColor(EBgColor.BLACK)
                            mPresenter.onBgChange()
                        }
                    }
                })

                seekBar.progress = PrefUtil.getSeekBarProgress()
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        mWebView.settings.textZoom = getRoom(p1)
                        PrefUtil.saveSeekBarProgress(p1)
                    }
                })
            }
            btDialog.show()
            true
        }

    }

    override fun getRxLifecycle(): RxLifecycle {
        return RxLifecycle.bind(this)
    }

    override fun showLoadError() {
        Toast.makeText(this, getText(R.string.load_error), Toast.LENGTH_SHORT).show()
    }

    override fun showPage(htmlBody: String) {
        mWebView.loadDataWithBaseURL(null, htmlBody, "text/html", "utf-8", null)
    }

    fun getRoom(progress: Int): Int {
        return when (progress) {
            0 -> 85
            1 -> 90
            2 -> 95
            3 -> 100
            4 -> 105
            5 -> 110
            6 -> 115
            else -> 100
        }
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {

        fun startSelf(context: Context) {
            val intent = Intent(context, DailyArticleActivity::class.java)

            when (PrefUtil.getActivityAnim()) {
                PrefUtil.ACTIVITY_ANIM_FADE_IN_FADE_OUT -> {
                    context.startActivity(intent)
                    (context as Activity).overridePendingTransition(android.R.anim.fade_in, 0)
                }
                PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_NORMAL -> {
                    context.startActivity(intent)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_normal, 0)
                }
                PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_FAST -> {
                    context.startActivity(intent)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_fast, 0)
                }
                PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_SLOW -> {
                    context.startActivity(intent)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_slow, 0)
                }
                else -> {
                    context.startActivity(intent)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_normal, 0)
                }

            }
        }
    }

}
