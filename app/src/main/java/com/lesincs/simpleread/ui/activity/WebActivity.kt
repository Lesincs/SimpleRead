package com.lesincs.simpleread.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseActivity
import com.lesincs.simpleread.util.PrefUtil
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.common_toolbar.*

class WebActivity : BaseActivity() {

    private lateinit var url: String

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.loading)

        Slidr.attach(this)

        url = intent.getStringExtra(EXTRA_WEB_URL)
        webViewAW.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title = title
            }
        }
        webViewAW.webViewClient = WebViewClient()
        webViewAW.settings.javaScriptEnabled = true
        webViewAW.loadUrl(url)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu_activity_web, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishSelf()
            }
            R.id.action_open_with_browser -> {
                val intent = Intent().setAction(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finishSelf()
    }

    companion object {
        fun startSelf(context: Context, webUrl: String) {
            val intent = Intent(context, CollectionActivity::class.java)
            intent.putExtra(EXTRA_WEB_URL, webUrl)
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
