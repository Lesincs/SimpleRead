package com.lesincs.simpleread.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseFrag
import com.lesincs.simpleread.bean.DBZHNews
import com.lesincs.simpleread.custom.CustomScaleAnimation
import com.lesincs.simpleread.dao.DBZHNewsDaoUtil
import com.lesincs.simpleread.ui.activity.ZHNewsDetailActivity
import com.lesincs.simpleread.ui.adapter.ZHCollectedNewsListAdapter
import com.lesincs.simpleread.util.PrefUtil
import kotlinx.android.synthetic.main.frag_collection.*

/**
 * Created by Administrator on 2018/1/24.
 */
class ZHCollectionFrag : BaseFrag() {

    private lateinit var adapter: ZHCollectedNewsListAdapter

    override fun getLayoutId(): Int {
        return R.layout.frag_collection
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewFC.layoutManager = LinearLayoutManager(context)
        initAdapter()
        recyclerViewFC.adapter = adapter
    }

    private fun initAdapter() {
        adapter = ZHCollectedNewsListAdapter(DBZHNewsDaoUtil.getCollectionNewsList())
        adapter.isFirstOnly(PrefUtil.itemAnimIsFirsOnly())
        when (PrefUtil.getItemAnim()) {
            PrefUtil.ITEM_ANIM_SLIDE_IN_LEFT -> adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            PrefUtil.ITEM_ANIM_SLIDE_IN_RIGHT -> adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
            PrefUtil.ITEM_ANIM_SLIDE_IN_BOTTOM -> adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
            PrefUtil.ITEM_ANIM_ALPHA -> adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
            PrefUtil.ITEM_ANIM_SCALE -> adapter.openLoadAnimation(CustomScaleAnimation())
        }
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            ZHNewsDetailActivity.startSelf(context!!, (adapter.data[position] as DBZHNews).newsId)
        }
        adapter.onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
            val pouMenu = PopupMenu(view.context, view, Gravity.END)
            val dbZHNews = adapter.data[position] as DBZHNews
            pouMenu.menuInflater.inflate(R.menu.poumenu_cancel_collection, pouMenu.menu)
            pouMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.pop_cancel_collection -> {
                        DBZHNewsDaoUtil.changeCollectionState(dbZHNews.newsId, false)
                        if (position == 0) {
                            adapter.notifyItemRemoved(0)
                            adapter.notifyItemChanged(0)
                        } else {
                            adapter.notifyItemRemoved(position)
                        }
                    }
                }
                false
            }
            pouMenu.show()
            false
        }
    }
}