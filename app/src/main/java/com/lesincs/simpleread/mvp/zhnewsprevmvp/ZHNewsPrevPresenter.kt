package com.lesincs.simpleread.mvp.zhnewsprevmvp

import com.lesincs.simpleread.util.CalenderUtil
import java.util.*

/**
 * Created by Administrator on 2017/12/24.
 */
class ZHNewsPrevPresenter(val view: ZHNewsPrevContract.View) : ZHNewsPrevContract.Presenter {

    private val model = ZHNewsPrevModel
    var isLoading = false

    override fun onStart() {
        view.showRefreshIndicator()
        model.getLatestNewsListObs(view.getRxLifecycle())
                .subscribe({
                    view.showNewsList(model.getNewsListItemList(it))
                    view.updateBanner(it.top_stories)
                    view.hideRefreshIndicator()
                }, {
                    view.snackLoadError()
                    view.hideRefreshIndicator()
                })
    }

    override fun onLoadMore(sDate: String) {
        isLoading = true
        model.getBeforeDateNewsListObs(sDate, view.getRxLifecycle())
                .subscribe({
                    if (it.stories == null) {
                        view.snackLoadError()
                        isLoading = false
                        return@subscribe
                    }
                    view.showMoreNewsList(model.getNewsListItemList(it))
                    view.hideRefreshIndicator()
                    isLoading = false
                }, {
                    view.snackLoadError()
                    isLoading = false
                })
    }

    override fun onRefresh() {
        model.getLatestNewsListObs(view.getRxLifecycle())
                .subscribe({
                    view.showNewsList(model.getNewsListItemList(it))
                    view.updateBanner(it.top_stories)
                    view.hideRefreshIndicator()
                }, {
                    view.snackLoadError()
                    view.hideRefreshIndicator()
                })
    }

    override fun onRandomPage() {
        view.showRefreshIndicator()
        model.getBeforeDateNewsListObs(CalenderUtil.getARandomZHDate(), view.getRxLifecycle())
                .subscribe({
                    view.hideRefreshIndicator()
                    view.showRandomPage(it.stories[Random().nextInt(it.stories.size)].id)
                    view.showRandomPageDate(it.date)
                }, {
                    view.snackLoadError()
                    view.hideRefreshIndicator()
                })
    }

    override fun onSpecificDate(date: String) {
        view.showRefreshIndicator()
        model.getBeforeDateNewsListObs(date, view.getRxLifecycle())
                .subscribe({
                    view.showNewsList(model.getNewsListItemList(it))
                    view.hideRefreshIndicator()
                }, {
                    view.snackLoadError()
                    view.hideRefreshIndicator()
                })
    }

}