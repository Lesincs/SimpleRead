package com.lesincs.simpleread.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import cn.nekocode.rxlifecycle.RxLifecycle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseFrag
import com.lesincs.simpleread.base.LazyInitFragment
import com.lesincs.simpleread.bean.DBZHNews
import com.lesincs.simpleread.bean.NewsPrevItem
import com.lesincs.simpleread.bean.TopStory
import com.lesincs.simpleread.custom.*
import com.lesincs.simpleread.dao.DBZHNewsDaoUtil
import com.lesincs.simpleread.mvp.zhnewsprevmvp.ZHNewsPrevContract
import com.lesincs.simpleread.mvp.zhnewsprevmvp.ZHNewsPrevPresenter
import com.lesincs.simpleread.ui.activity.EXTRA_PAGE_ID
import com.lesincs.simpleread.ui.activity.ZHNewsDetailActivity
import com.lesincs.simpleread.ui.adapter.ZHNewsListAdapter
import com.lesincs.simpleread.util.CalenderUtil
import com.lesincs.simpleread.util.PrefUtil
import kotlinx.android.synthetic.main.frag_zh_news_prev.*
import kotlinx.android.synthetic.main.item_news_prev.view.*
import kotlinx.android.synthetic.main.view_banner.*
import nov.lesincs.twogoods.cusotom.CirclePointIndicator


/**
 * Created by Administrator on 2017/12/15.
 */

const val DAILY_ARTICLE_PAGE_TYPE = "DAILY_ARTICLE_PAGE_TYPE"

enum class DailyArticlePageType {
    TODAYPAGE, RANDOMPAGE
}

class ZHNewsListFrag : ZHNewsPrevContract.View, BaseFrag() {

    private val newsListItems = ArrayList<NewsPrevItem>()
    private val topStories = ArrayList<TopStory>()
    private lateinit var adapter: ZHNewsListAdapter
    private val linearLayoutManager = LinearLayoutManager(context)
    val presenter = ZHNewsPrevPresenter(this)
    private lateinit var banner: CustomViewPager
    private lateinit var fabMenu: MyFloatingActionMenu
    private lateinit var circlePointIndicator:CirclePointIndicator
    private lateinit var tvDate:TextView

    override fun updateBanner(topStories: List<TopStory>) {
        this.topStories.clear()
        this.topStories.addAll(topStories)
        banner.update(this.topStories)
    }

    override fun snackLoadError() {
        val snackBar = Snackbar.make(recyclerViewNPF, "加载失败了...", Snackbar.LENGTH_LONG)
        snackBar.setAction("知道了", {
            snackBar.dismiss()
        })
        snackBar.show()
    }

    override fun showNewsList(newsListItems: List<NewsPrevItem>) {
        if (!this.newsListItems.containsAll(newsListItems)) {
            val previousItemCount = this.newsListItems.size
            this.newsListItems.clear()
            adapter.notifyItemRangeRemoved(1, previousItemCount)
            this.newsListItems.addAll(newsListItems)
            adapter.notifyItemRangeChanged(1, newsListItems.size)
        }

        recyclerViewNPF.post {  loadMoreIfNotFullPage() }
    }

    private fun loadMoreIfNotFullPage() {
        val lastComVisiblePos = linearLayoutManager.findLastCompletelyVisibleItemPosition()

        if (lastComVisiblePos + 1 == adapter.itemCount){
            presenter.onLoadMore(newsListItems.last().date)
        }

    }

    override fun showMoreNewsList(newsListItems: List<NewsPrevItem>) {
        val previousPosition = this.newsListItems.size
        this.newsListItems.addAll(newsListItems)
        adapter.notifyItemRangeInserted(previousPosition + 1, newsListItems.size)
    }

    override fun showRefreshIndicator() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun showRandomPage(pageId: String) {
        startActivity(Intent(context, ZHNewsDetailActivity::class.java).putExtra(EXTRA_PAGE_ID, pageId))
        activity!!.overridePendingTransition(R.anim.slide_in_right_fast, 0)
    }

    override fun showRandomPageDate(sDate: String) {
        val toast = Toast.makeText(context, CalenderUtil.zhDateToFriendlyDate2(sDate), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun getRxLifecycle(): RxLifecycle {
        return RxLifecycle.bind(context as Activity)
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_zh_news_prev
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter.onStart()
    }

    private fun initView() {
        recyclerViewNPF.layoutManager = linearLayoutManager
        tvDate = view!!.findViewById(R.id.tvDate)

        val bannerParent = LayoutInflater.from(context).inflate(R.layout.view_banner, recyclerViewNPF.parent as ViewGroup, false)
        circlePointIndicator =bannerParent.findViewById(R.id.cpiNPF)
        initAdapter()
        recyclerViewNPF.adapter = adapter
        adapter.addHeaderView(bannerParent)
        banner = bannerParent.findViewById(R.id.viewPagerNPF)
        banner.init(this)
        recyclerViewNPF.addOnScrollListener(onScrollListener)
        swipeRefreshLayout.setColorSchemeColors(getAccentColor())
        swipeRefreshLayout.setOnRefreshListener { presenter.onRefresh() }
        fabMenu = activity!!.findViewById(R.id.fabMenu)
        circlePointIndicator.bindToViewPager(banner)
    }

    private fun initAdapter() {
        adapter = ZHNewsListAdapter(newsListItems)
        adapter.isFirstOnly(PrefUtil.itemAnimIsFirsOnly())
        when (PrefUtil.getItemAnim()) {
            PrefUtil.ITEM_ANIM_SLIDE_IN_LEFT -> adapter.openLoadAnimation(CustomSlideInLeftAnimation())
            PrefUtil.ITEM_ANIM_SLIDE_IN_RIGHT -> adapter.openLoadAnimation(CustomSlideInRightAnimation())
            PrefUtil.ITEM_ANIM_SLIDE_IN_BOTTOM -> {
                adapter.isFirstOnly(true)
                adapter.openLoadAnimation(CustomSlideInBottomAnimation())
            }
            PrefUtil.ITEM_ANIM_ALPHA -> adapter.openLoadAnimation(CustomAlphaAnimation())
            PrefUtil.ITEM_ANIM_SCALE -> adapter.openLoadAnimation(CustomScaleAnimation())
        }
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            with(view) {
                tvTitle.setTextColor(android.graphics.Color.argb(145, 0, 0, 0))
                ZHNewsDetailActivity.startSelf(context, newsListItems[position].id)
            }
        }
        adapter.setOnItemLongClickListener { adapter, view, position ->
            val newsItem = newsListItems[position]
            val pouMenu = PopupMenu(context!!, view, Gravity.END)

            if (DBZHNewsDaoUtil.isNewsCollected(newsItem.id)) {
                pouMenu.menuInflater.inflate(R.menu.poumenu_cancel_collection, pouMenu.menu)
            } else {
                pouMenu.menuInflater.inflate(R.menu.popmenu_collection, pouMenu.menu)
            }
            pouMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.pop_cancel_collection -> {
                        Snackbar.make(view, "取消收藏", Snackbar.LENGTH_SHORT).show()
                        DBZHNewsDaoUtil.changeCollectionState(newsItem.id, false)
                    }
                    R.id.pop_collection -> {
                        Snackbar.make(view, "已收藏", Snackbar.LENGTH_SHORT).show()
                        if (DBZHNewsDaoUtil.isNewsInDB(newsItem.id)) {
                            DBZHNewsDaoUtil.changeCollectionState(newsItem.id, true)
                        } else {
                            val dbZHNews = DBZHNews()
                            dbZHNews.collectTime = System.currentTimeMillis()
                            dbZHNews.imageUrl = newsItem.imageUrl
                            dbZHNews.isCollected = true
                            dbZHNews.isRead = false
                            dbZHNews.newsTitle = newsItem.title
                            dbZHNews.newsId = newsItem.id
                            DBZHNewsDaoUtil.insertToDB(dbZHNews)
                        }
                    }
                }
                false
            }
            pouMenu.show()
            false
        }
    }

    private fun getAccentColor(): Int {
        val colorAccentTV = TypedValue()
        activity!!.theme.resolveAttribute(R.attr.colorAccent, colorAccentTV, true)
        return resources.getColor(colorAccentTV.resourceId)
    }

    fun changeSwipeColor(color: Int) {
        swipeRefreshLayout?.setColorSchemeColors(color)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {

        val THRESHOLD = 175
        var isAnim = false
        var direOffset = 0
        var isPullUp = false

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isPullUp = dy > 0
            if (direOffset > 0 && dy < 0 || direOffset < 0 && dy > 0) {
                direOffset = 0
            }
            direOffset += dy
            if (direOffset > THRESHOLD && fabMenu.visibility == View.VISIBLE && !isAnim) {
                fabMenu.customHide()
            }
            if (direOffset < THRESHOLD * (-1) && fabMenu.visibility == View.INVISIBLE && !isAnim) {
                fabMenu.customShow()
            }

            if (recyclerViewNPF.childCount != 0){
                val firstPosition = recyclerViewNPF.getChildAdapterPosition(recyclerViewNPF.getChildAt(0))

                if (firstPosition == 0){
                    tvDate.visibility = View.INVISIBLE
                }else{
                    tvDate.visibility = View.VISIBLE
                    tvDate.text = CalenderUtil.zhDateToFriendlyDate(newsListItems[firstPosition-1].date)
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
            val totalItemPosition = linearLayoutManager.itemCount - 1
            if (!presenter.isLoading && isPullUp && lastVisibleItemPosition >= totalItemPosition - 4) {
                presenter.onLoadMore(newsListItems.last().date)
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                direOffset = 0
                isPullUp = false
            }
        }
    }

    override fun hideRefreshIndicator() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            adapter.openLoadAnimation(10)
            adapter.notifyDataSetChanged()
            when (PrefUtil.getItemAnim()) {
                PrefUtil.ITEM_ANIM_SLIDE_IN_LEFT -> adapter.openLoadAnimation(CustomSlideInLeftAnimation())
                PrefUtil.ITEM_ANIM_SLIDE_IN_RIGHT -> adapter.openLoadAnimation(CustomSlideInRightAnimation())
                PrefUtil.ITEM_ANIM_SLIDE_IN_BOTTOM -> adapter.openLoadAnimation(CustomSlideInBottomAnimation())
                PrefUtil.ITEM_ANIM_ALPHA -> adapter.openLoadAnimation(CustomAlphaAnimation())
                PrefUtil.ITEM_ANIM_SCALE -> adapter.openLoadAnimation(CustomScaleAnimation())
            }
        }
    }


}