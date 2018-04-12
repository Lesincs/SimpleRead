package com.lesincs.simpleread.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseActivity
import com.lesincs.simpleread.util.PrefUtil
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.common_toolbar.*

class InfoActivity : BaseActivity() {

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.toolbar_about)

        Slidr.attach(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finishSelf()
    }

    companion object {

        fun startSelf(context: Context) {

            val intent = Intent(context, InfoActivity::class.java)

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
