package com.lesincs.simpleread.ui.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lesincs.simpleread.R
import com.lesincs.simpleread.bean.TopStory
import com.lesincs.simpleread.ui.activity.ZHNewsDetailActivity
import kotlinx.android.synthetic.main.item_banner.view.*

/**
 * Created by Administrator on 2017/12/23.
 */

val IS_BANNER_ADAPTER = "IS_BANNER"

class BannerAdapter(private val topStories: List<TopStory>, private val context: Context) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return topStories.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val topStory = topStories[position]
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_banner, container, false)
        with(view) {
            Glide.with(container.context).load(topStory.image).into(ivBannerImage)
            tvBannerText.text = topStory.title
        }
        view.setOnClickListener {
            ZHNewsDetailActivity.startSelfForResult(context, topStory.id)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

    }

}