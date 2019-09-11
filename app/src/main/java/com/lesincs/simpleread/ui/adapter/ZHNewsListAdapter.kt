package com.lesincs.simpleread.ui.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lesincs.simpleread.R
import com.lesincs.simpleread.bean.NewsPrevItem
import kotlinx.android.synthetic.main.item_news_prev.view.*
import kotlinx.android.synthetic.main.item_news_prev_with_date.view.*

/**
 * Created by Administrator on 2018/3/30.
 */
const val COLOR_GRAY = "#91000000"

class ZHNewsListAdapter(private val newsPrevItems: List<NewsPrevItem>) : BaseQuickAdapter<NewsPrevItem, BaseViewHolder>(R.layout.item_news_prev_with_date, newsPrevItems) {

    override fun convert(helper: BaseViewHolder, item: NewsPrevItem) {
        with(helper.itemView) {
            tvTitle.text = item.title
            com.bumptech.glide.Glide.with(context).load(item.imageUrl).crossFade().error(com.lesincs.simpleread.R.drawable.image_place_holder).into(ivImageUrl)
            //是够显示日期
            if (isShowDate(helper.adapterPosition - 1)) {
                tvDate.visibility = android.view.View.VISIBLE
            } else {
                tvDate.visibility = android.view.View.GONE
            }
            tvDate.text = com.lesincs.simpleread.util.CalenderUtil.zhDateToFriendlyDate(item.date)
            //字体颜色
            if (com.lesincs.simpleread.dao.DBZHNewsDaoUtil.isNewsRead(item.id)) {
                tvTitle.setTextColor(Color.parseColor(COLOR_GRAY))
            } else {
                tvTitle.setTextColor(Color.BLACK)
            }
        }
    }

    private fun isShowDate(position: Int): Boolean {
        if (position == 0)
            return true
        val currentDate = newsPrevItems[position].date
        val prevDate = newsPrevItems[position - 1].date
        return currentDate != prevDate
    }
}