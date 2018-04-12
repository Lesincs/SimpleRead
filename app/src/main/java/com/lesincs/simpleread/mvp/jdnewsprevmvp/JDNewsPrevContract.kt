package com.lesincs.simpleread.mvp.jdnewsprevmvp

import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.JDNewsPrevBean
import com.lesincs.simpleread.bean.Post
import io.reactivex.Observable

/**
 * Created by Administrator on 2018/1/23.
 */
interface JDNewsPrevContract {

    interface Presenter {
        fun onStart()
        fun onLoadMore(page: Int)
        fun onRefresh()
    }

    interface Model {
        fun getLatestNewsObs(rxLifecycle: RxLifecycle): Observable<JDNewsPrevBean>
        fun getBeforeNewsObs(page: Int, rxLifecycle: RxLifecycle): Observable<JDNewsPrevBean>
    }

    interface View {
        fun showRefreshIndicator()
        fun hideRefreshIndicator()
        fun showLatestFreshNews(posts: List<Post>)
        fun showMoreFreshNews(posts: List<Post>)
        fun snackLoadError()
        fun getRxLifecycle(): RxLifecycle
    }
}