package com.lesincs.simpleread.ui.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lesincs.simpleread.R
import com.lesincs.simpleread.bean.Post
import com.lesincs.simpleread.util.CalenderUtil
import kotlinx.android.synthetic.main.item_news_prev.view.*
import kotlinx.android.synthetic.main.item_news_prev_with_date.view.*

/**
 * Created by Administrator on 2018/3/31.
 */
class JDNewsListAdapter(private val posts: List<Post>) : BaseQuickAdapter<Post, BaseViewHolder>(R.layout.item_news_prev_with_date, posts) {

    override fun convert(helper: BaseViewHolder, item: Post) {
        with(helper.itemView) {
            if (isShowDate(helper.layoutPosition)) {
                tvDate.visibility = android.view.View.VISIBLE
            } else {
                tvDate.visibility = android.view.View.GONE
            }
            tvDate.text = com.lesincs.simpleread.util.CalenderUtil.jdDateToFriendlyDate(item.date)
            tvTitle.text = item.title
            com.bumptech.glide.Glide.with(context).load(item.custom_fields.thumb_c[0]).into(ivImageUrl)
            if (com.lesincs.simpleread.dao.DBJDNewsDaoUtil.isNewsRead(item.id)) {
                tvTitle.setTextColor(Color.parseColor(COLOR_GRAY))
            } else {
                tvTitle.setTextColor(Color.BLACK)
            }
        }
    }

    private fun isShowDate(position: Int): Boolean {
        if (position == 0)
            return true
        val currentDate = CalenderUtil.jdDateToComparableString(posts[position].date)
        val prevDate = CalenderUtil.jdDateToComparableString(posts[position - 1].date)
        return currentDate != prevDate
    }
}