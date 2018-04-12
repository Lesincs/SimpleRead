package com.lesincs.simpleread.mvp.jdnewsprevmvp

import android.util.Log

/**
 * Created by Administrator on 2018/1/23.
 */
class JDNewsPrevPresenter(val view: JDNewsPrevContract.View) : JDNewsPrevContract.Presenter {

    private val model = JDNewsPrevModel
    var isLoading = false

    override fun onStart() {
        view.showRefreshIndicator()
        model.getLatestNewsObs(view.getRxLifecycle())
                .subscribe({
                    view.hideRefreshIndicator()
                    view.showLatestFreshNews(it.posts)
                }, {
                    view.hideRefreshIndicator()
                    view.snackLoadError()
                })
    }

    override fun onLoadMore(page: Int) {
        isLoading = true
        Log.d("page", page.toString())
        model.getBeforeNewsObs(page, view.getRxLifecycle())
                .subscribe({
                    view.showMoreFreshNews(it.posts)
                    isLoading = false
                }, {
                    view.snackLoadError()
                    isLoading = false
                })
    }

    override fun onRefresh() {
        model.getLatestNewsObs(view.getRxLifecycle())
                .subscribe({
                    view.hideRefreshIndicator()
                    view.showLatestFreshNews(it.posts)
                }, {
                    view.hideRefreshIndicator()
                    view.snackLoadError()
                })
    }
}