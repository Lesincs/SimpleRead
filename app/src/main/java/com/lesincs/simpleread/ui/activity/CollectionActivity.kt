package com.lesincs.simpleread.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseActivity
import com.lesincs.simpleread.ui.adapter.FragAdapterWithTableLayout
import com.lesincs.simpleread.ui.fragment.JDCollectionFrag
import com.lesincs.simpleread.ui.fragment.ZHCollectionFrag
import com.lesincs.simpleread.util.PrefUtil
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.common_toolbar.*

class CollectionActivity : BaseActivity() {

    override fun afterOnCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbarCollection)
        supportActionBar?.setTitle(R.string.toolbar_collection)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*配置滑动返回在边缘生效 避免与ViewPager冲突*/
        Slidr.attach(this,
                SlidrConfig.Builder()
                        .edge(true)
                        .edgeSize(0.30f)
                        .build())

        /*初始化TabLayout和ViewPager*/
        viewPagerAC.adapter = FragAdapterWithTableLayout(supportFragmentManager, listOf(ZHCollectionFrag(), JDCollectionFrag()), listOf(getString(R.string.toolbar_zh_frag), getString(R.string.toolbar_jd_frag)))
        viewPagerAC.offscreenPageLimit = 1
        tableLayoutAC.setupWithViewPager(viewPagerAC, true)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_collection
    }

    override fun onBackPressed() {
        finishSelf()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishSelf()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun startSelf(context: Context) {

            val intent = Intent(context, CollectionActivity::class.java)

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
