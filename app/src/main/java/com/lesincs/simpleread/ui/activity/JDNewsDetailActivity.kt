package com.lesincs.simpleread.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import com.lesincs.simpleread.dao.DBJDNewsDaoUtil
import com.lesincs.simpleread.mvp.jdnewsdetailmvp.JDNewsDetailContract
import com.lesincs.simpleread.mvp.jdnewsdetailmvp.JDNewsDetailPresenter
import com.lesincs.simpleread.util.CalenderUtil
import com.lesincs.simpleread.util.PrefUtil
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_news_detail.*

const val EXTRA_NEWS_TITLE = "EXTRA_NEWS_TITLE"
const val EXTRA_NEWS_IMAGE_URL = "EXTRA_NEWS_IMAGE_URL"
const val EXTRA_NEWS_ID = "EXTRA_NEWS_ID"
const val EXTRA_NEWS_AUTHOR = "EXTRA_NEWS_AUTHOR"
const val EXTRA_NEWS_DATE = "EXTRA_NEWS_DATE"
const val EXTRA_NEWS_URL = "EXTRA_NEWS_URL"

class JDFreshNewsDetailActivity : JDNewsDetailContract.View, BaseActivity() {

    private val mPresenter = JDNewsDetailPresenter(this)
    private lateinit var extraNewsTitle: String
    private lateinit var extraNewsId: String
    private lateinit var extraImageUrl: String
    private lateinit var extraNewsAuthor: String
    private lateinit var extraNewsDate: String
    private lateinit var extraNewsUrl: String

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        extraNewsTitle = intent.getStringExtra(EXTRA_NEWS_TITLE)
        extraNewsId = intent.getStringExtra(EXTRA_NEWS_ID)
        extraImageUrl = intent.getStringExtra(EXTRA_NEWS_IMAGE_URL)
        extraNewsAuthor = intent.getStringExtra(EXTRA_NEWS_AUTHOR)
        extraNewsDate = intent.getStringExtra(EXTRA_NEWS_DATE)
        extraNewsUrl = intent.getStringExtra(EXTRA_NEWS_URL)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Slidr.attach(this)

        mPresenter.onStart(extraNewsId, extraNewsTitle, extraImageUrl, extraNewsUrl, extraNewsDate, extraNewsAuthor)

        webView.isScrollbarFadingEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = false
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheEnabled(false)
        webView.settings.supportZoom()
        webView.settings.textZoom = 110

        if (PrefUtil.getIsOpenLinkWithLocalBrowser()) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    WebActivity.startSelf(this@JDFreshNewsDetailActivity, request?.url.toString())
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
    }

    override fun showExtraNewsTitle() {
        ctl.title = extraNewsTitle
    }

    override fun loadExtraImageUrl() {
        Glide.with(this).load(extraImageUrl).crossFade().into(ivImageUrl)
    }

    override fun showExtraAuthorNameAndPostDate() {
        tvImageSource.text = "@" + extraNewsAuthor + " " + CalenderUtil.jdDateToFriendlyDate2(extraNewsDate)
    }

    override fun snackCollectSuccess() {
        snack(getString(R.string.collect_success), webView)
    }

    override fun snackCancelCollection() {
        snack(getString(R.string.cancel_collection), webView)
    }


    override fun loadHtmlContent(htmlContent: String) {
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }

    override fun getRxLifecycle(): RxLifecycle {
        return RxLifecycle.bind(this)
    }

    override fun snackCopySuccess() {
        snack(getString(R.string.copy_success), webView)
    }

    override fun snackLoadError() {
        val snackBar = Snackbar.make(webView, R.string.load_error, Snackbar.LENGTH_LONG)
        snackBar.setAction(R.string.i_know_it, {
            snackBar.dismiss()
        })
        snackBar.show()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_news_detail
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishSelf()
            R.id.action_share -> mPresenter.onShare(extraNewsUrl, extraNewsTitle)
            R.id.action_copy_link -> mPresenter.onCopyLink(extraNewsUrl)
            R.id.action_collect -> mPresenter.onCollected(extraNewsId, item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_news_detail, menu)
        menu.findItem(R.id.action_collect).isChecked = DBJDNewsDaoUtil.isNewsCollected(extraNewsId)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        finishSelf()
    }

    companion object {

        fun startSelf(context: Context, pageId: String?, title: String?, imageUrl: String?, author: String?, date: String?, pageUrl: String?) {
            val intent = Intent(context, JDFreshNewsDetailActivity::class.java)
            intent.putExtra(EXTRA_NEWS_AUTHOR, author)
            intent.putExtra(EXTRA_NEWS_IMAGE_URL, imageUrl)
            intent.putExtra(EXTRA_NEWS_ID, pageId)
            intent.putExtra(EXTRA_NEWS_TITLE, title)
            intent.putExtra(EXTRA_NEWS_DATE, date)
            intent.putExtra(EXTRA_NEWS_URL, pageUrl)

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
