package com.lesincs.simpleread.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lesincs.simpleread.R
import com.lesincs.simpleread.bean.DBZHNews
import kotlinx.android.synthetic.main.item_news_prev.view.*
import kotlinx.android.synthetic.main.item_news_prev_with_date.view.*
import java.util.*

/**
 * Created by Administrator on 2018/3/31.
 */
class ZHCollectedNewsListAdapter(val dbZHNewss: List<DBZHNews>) : BaseQuickAdapter<DBZHNews, BaseViewHolder>(R.layout.item_news_prev_with_date, dbZHNewss) {

    override fun convert(helper: BaseViewHolder, item: DBZHNews) {
        with(helper.itemView) {
            tvTitle.text = item.newsTitle
            com.bumptech.glide.Glide.with(context).load(item.imageUrl).placeholder(com.lesincs.simpleread.R.drawable.image_place_holder).crossFade().into(ivImageUrl)
            tvDate.text = context.getText(R.string.collect_time).toString() + com.lesincs.simpleread.util.CalenderUtil.getCollectionFriendlyDate(item.collectTime)
            if (isShowDate(helper.layoutPosition)) {
                tvDate.visibility = android.view.View.VISIBLE
            } else {
                tvDate.visibility = android.view.View.GONE
            }
        }
    }

    private fun longTimeToString(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE)
    }

    private fun isShowDate(position: Int): Boolean {
        return if (position == 0)
            true
        else {
            val currentTime = longTimeToString(dbZHNewss[position].collectTime)
            val previousTime = longTimeToString(dbZHNewss[position - 1].collectTime)
            currentTime != previousTime
        }
    }
}