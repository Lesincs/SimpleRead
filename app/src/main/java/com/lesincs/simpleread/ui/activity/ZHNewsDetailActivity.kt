package com.lesincs.simpleread.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.nekocode.rxlifecycle.RxLifecycle
import com.bumptech.glide.Glide
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseActivity
import com.lesincs.simpleread.dao.DBZHNewsDaoUtil
import com.lesincs.simpleread.mvp.zhnewsdetailmvp.ZHNewsDetailContract
import com.lesincs.simpleread.mvp.zhnewsdetailmvp.ZHNewsDetailPresenter
import com.lesincs.simpleread.ui.adapter.IS_BANNER_ADAPTER
import com.lesincs.simpleread.util.PrefUtil
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_news_detail.*

val EXTRA_WEB_URL = "EXTRA_WEB_URL"
val EXTRA_PAGE_ID = "EXTRA_PAGE_ID"

class ZHNewsDetailActivity : ZHNewsDetailContract.View, BaseActivity() {
    override fun showNewsImageSource(imageSource: String) {
        tvImageSource.text = imageSource
    }

    override fun snackCollectSuccess() {
        snack(getString(R.string.collect_success), webView)
    }

    override fun snackCancelCollection() {
        snack(getString(R.string.cancel_collection), webView)
    }

    override fun loadImageUrl(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(ivImageUrl)
    }

    override fun showNewsTitle(newsTitle: String) {
        ctl.title = newsTitle
    }


    override fun loadHtmlContent(htmlContent: String) {
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }

    override fun getRxLifecycle(): RxLifecycle {
        return RxLifecycle.bind(this)
    }

    private val presenter = ZHNewsDetailPresenter(this)

    override fun getLayoutId(): Int {
        return R.layout.activity_news_detail
    }

    override fun snackLoadError() {
        val snackBar = Snackbar.make(webView, R.string.load_error, Snackbar.LENGTH_LONG)
        snackBar.setAction(R.string.i_know_it, {
            snackBar.dismiss()
        })
        snackBar.show()
    }


    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Slidr.attach(this)
        presenter.onStart(intent.getStringExtra(EXTRA_PAGE_ID))
        if (PrefUtil.getIsOpenLinkWithLocalBrowser()) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val intent = Intent(this@ZHNewsDetailActivity, WebActivity::class.java)
                    intent.putExtra(EXTRA_WEB_URL, request?.url.toString())
                    startActivity(intent)
                    return true
                }
            }
        } else {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val intent = Intent(Intent.ACTION_VIEW).setData(request?.url)
                    startActivity(intent)
                    return true
                }
            }
        }
        webView.isScrollbarFadingEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = false
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_share -> presenter.onShare()
            R.id.action_copy_link -> presenter.onCopyLink()
            R.id.action_collect -> presenter.onCollected(intent.getStringExtra(EXTRA_PAGE_ID), item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_news_detail, menu)
        menu.findItem(R.id.action_collect).isChecked = DBZHNewsDaoUtil.isNewsCollected(intent.getStringExtra(EXTRA_PAGE_ID))
        return super.onCreateOptionsMenu(menu)
    }

    override fun snackCopySuccess() {
        snack(getString(R.string.copy_success), webView)
    }

    override fun onBackPressed() {
        intent.getStringExtra(IS_BANNER_ADAPTER)?.let {
            setResult(1)
        }
        finishSelf()
    }

    companion object {
        fun startSelf(context: Context, pageId: String) {
            val intent = Intent(context, ZHNewsDetailActivity::class.java)
            intent.putExtra(EXTRA_PAGE_ID, pageId)
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

        fun startSelfForResult(context: Context, pageId: String) {
            val intent = Intent(context, ZHNewsDetailActivity::class.java)
            val appCompatActivity = context as AppCompatActivity
            intent.putExtra(EXTRA_PAGE_ID, pageId)
            intent.putExtra(IS_BANNER_ADAPTER, IS_BANNER_ADAPTER)
            when (PrefUtil.getActivityAnim()) {
                PrefUtil.ACTIVITY_ANIM_FADE_IN_FADE_OUT -> {
                    appCompatActivity.startActivityForResult(intent, 1)
                    (context as Activity).overridePendingTransition(android.R.anim.fade_in, 0)
                }
                PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_NORMAL -> {
                    appCompatActivity.startActivityForResult(intent, 1)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_normal, 0)
                }
                PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_FAST -> {
                    appCompatActivity.startActivityForResult(intent, 1)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_fast, 0)
                }
                PrefUtil.ACTIVITY_ANIM_SLIDE_IN_SLIDE_OUT_SLOW -> {
                    appCompatActivity.startActivityForResult(intent, 1)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_slow, 0)
                }
                else -> {
                    appCompatActivity.startActivityForResult(intent, 1)
                    (context as Activity).overridePendingTransition(R.anim.slide_in_right_normal, 0)
                }

            }
        }
    }
}
