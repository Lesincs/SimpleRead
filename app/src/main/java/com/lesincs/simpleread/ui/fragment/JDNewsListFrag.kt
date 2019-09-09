package com.lesincs.simpleread.ui.fragment

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import cn.nekocode.rxlifecycle.RxLifecycle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lesincs.simpleread.R
import com.lesincs.simpleread.base.BaseFrag
import com.lesincs.simpleread.bean.DBJDNews
import com.lesincs.simpleread.bean.Post
import com.lesincs.simpleread.custom.*
import com.lesincs.simpleread.dao.DBJDNewsDaoUtil
import com.lesincs.simpleread.mvp.jdnewsprevmvp.JDNewsPrevContract
import com.lesincs.simpleread.mvp.jdnewsprevmvp.JDNewsPrevPresenter
import com.lesincs.simpleread.ui.activity.JDFreshNewsDetailActivity
import com.lesincs.simpleread.ui.adapter.JDNewsListAdapter
import com.lesincs.simpleread.util.CalenderUtil
import com.lesincs.simpleread.util.PrefUtil
import kotlinx.android.synthetic.main.frag_jd_fresh_news_prev.*
import kotlinx.android.synthetic.main.item_news_prev.*

/**
 * Created by Administrator on 2018/1/23.
 */
class JDNewsListFrag : BaseFrag(), JDNewsPrevContract.View {
    override fun getRxLifecycle(): RxLifecycle {
        return RxLifecycle.bind(context as Activity)
    }

    private val presenter = JDNewsPrevPresenter(this)
    private val posts = ArrayList<Post>()
    private lateinit var  adapter:JDNewsListAdapter
    private val linearLayoutManager = LinearLayoutManager(context)
    private var currentPage = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter.onStart()
    }

    private fun getAccentColor(): Int {
        val colorAccentTV = TypedValue()
        activity!!.theme.resolveAttribute(R.attr.colorAccent,colorAccentTV,true)
        return resources.getColor(colorAccentTV.resourceId)
    }
    fun changeSwipeColor(color:Int){
        swipeRefreshLayoutFJDFNP?.setColorSchemeColors(color)
    }
    private fun initView() {
        initAdapter()
        recyclerViewFJDFNP.adapter = adapter
        recyclerViewFJDFNP.layoutManager = linearLayoutManager
        recyclerViewFJDFNP.addOnScrollListener(onScrollListener)
        swipeRefreshLayoutFJDFNP.setColorSchemeColors(getAccentColor())
        swipeRefreshLayoutFJDFNP.setOnRefreshListener { presenter.onRefresh() }
    }

    private fun initAdapter() {
        adapter = JDNewsListAdapter(posts)
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
                with(view){
                    tvTitle.setTextColor(Color.argb(145, 0, 0, 0))
                }
            val post = posts[position]
            JDFreshNewsDetailActivity.startSelf(context!!,post.id,post.title,post.custom_fields.thumb_c[0],post.author.name,post.date,post.url)
        }
        adapter.onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
            val post = posts[position]
            val pouMenu = PopupMenu(context!!, view, Gravity.END)
            if (DBJDNewsDaoUtil.isNewsCollected(post.id)) {
                pouMenu.menuInflater.inflate(R.menu.poumenu_cancel_collection, pouMenu.menu)
            } else {
                pouMenu.menuInflater.inflate(R.menu.popmenu_collection, pouMenu.menu)
            }
            pouMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.pop_cancel_collection -> {
                        Snackbar.make(view, "取消收藏", Snackbar.LENGTH_SHORT).show()
                        DBJDNewsDaoUtil.changeCollectionState(post.id, false)
                    }
                    R.id.pop_collection -> {
                        Snackbar.make(view, "已收藏", Snackbar.LENGTH_SHORT).show()
                        if (DBJDNewsDaoUtil.isNewsInDB(post.id)) {
                            DBJDNewsDaoUtil.changeCollectionState(post.id, true)
                        } else {
                            val dbJDNews = DBJDNews()
                            dbJDNews.isCollected = true
                            dbJDNews.collectTime = System.currentTimeMillis()
                            dbJDNews.newsId = post.id
                            dbJDNews.isRead = true
                            dbJDNews.imageUrl = post.custom_fields.thumb_c[0]
                            dbJDNews.newsTitle = post.title
                            dbJDNews.newsUrl =post.url
                            dbJDNews.postDate=post.date
                            dbJDNews.authorName=post.author.name
                            DBJDNewsDaoUtil.insertToDB(dbJDNews)
                        }
                    }
                }
                false
            }
            pouMenu.show()
            false
        }
    }

    override fun showRefreshIndicator() {
        swipeRefreshLayoutFJDFNP.isRefreshing = true
    }

    override fun hideRefreshIndicator() {
        swipeRefreshLayoutFJDFNP?.isRefreshing = false
    }

    override fun showLatestFreshNews(posts: List<Post>) {
        if (!this.posts.containsAll(posts)) {
            val previousItemCount = this.posts.size
            this.posts.clear()
            adapter.notifyItemRangeRemoved(0, previousItemCount)
            this.posts.addAll(posts)
            adapter.notifyItemRangeChanged(0, posts.size)
        }
    }

    override fun showMoreFreshNews(posts: List<Post>) {
        val position = this.posts.size
        this.posts.addAll(posts)
       adapter.notifyItemRangeChanged(position,posts.size)
    }

    override fun snackLoadError() {
        Snackbar.make(recyclerViewFJDFNP, "加载错误...", Snackbar.LENGTH_SHORT).show()
    }

    override fun getLayoutId(): Int {
        return R.layout.frag_jd_fresh_news_prev
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {

        var isPullUp = true

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isPullUp = dy > 0

            if (recyclerViewFJDFNP.childCount != 0){
                tvDateJD.visibility = View.VISIBLE
                val firstPosition = recyclerViewFJDFNP.getChildAdapterPosition(recyclerViewFJDFNP.getChildAt(0))
                tvDateJD.text = CalenderUtil.jdDateToFriendlyDate(posts[firstPosition].date)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (isPullUp && !presenter.isLoading&& linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1 >= posts.size - 3)
            {
                presenter.onLoadMore(++currentPage)
            }
        }

    }
}