package com.lesincs.simpleread.mvp.zhnewsprevmvp

import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.lesincs.simpleread.bean.NewsPrevItem
import com.lesincs.simpleread.bean.TopStory
import com.lesincs.simpleread.bean.ZHNewsPrevBean
import io.reactivex.Observable

/**
 * Created by Administrator on 2017/12/24.
 */
interface ZHNewsPrevContract {

    interface Presenter {
        fun onStart()
        fun onLoadMore(sDate: String)
        fun onRefresh()
        fun onRandomPage()
        fun onSpecificDate(date: String)

    }

    interface Model {
        fun getLatestNewsListObs(rxLifecycle: RxLifecycleCompact): Observable<ZHNewsPrevBean>
        fun getBeforeDateNewsListObs(sDate: String, rxLifecycle: RxLifecycleCompact): Observable<ZHNewsPrevBean>
        fun getNewsListItemList(newsListBean: ZHNewsPrevBean): List<NewsPrevItem>
    }

    interface View {
        fun showNewsList(newsListItems: List<NewsPrevItem>)
        fun updateBanner(topStories: List<TopStory>)
        fun showMoreNewsList(newsListItems: List<NewsPrevItem>)
        fun showRefreshIndicator()
        fun hideRefreshIndicator()
        fun showRandomPage(pageId: String)
        fun snackLoadError()
        fun showRandomPageDate(sDate: String)
        fun getRxLifecycle(): RxLifecycleCompact
    }
}