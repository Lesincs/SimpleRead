package com.lesincs.simpleread.mvp.dailyarticlemvp

import com.lesincs.simpleread.bean.DailyArticleBean
import com.lesincs.simpleread.ui.fragment.DailyArticlePageType

/**
 * Created by Administrator on 2017/12/27.
 */
class DailyArticlePresenter(val view: DailyArticleContract.View) : DailyArticleContract.Presenter {

    private val model = DailyArticleModel
    private lateinit var bean: DailyArticleBean

    override fun onStart(pageType: String) {
        when (pageType) {
            DailyArticlePageType.TODAYPAGE.toString() -> {
                model.getTodayArticleObs(view.getRxLifecycle())
                        .subscribe({
                            bean = it
                            view.showPage(model.getHtmlBody(it))
                        }, {
                            view.showLoadError()
                        })
            }

            DailyArticlePageType.RANDOMPAGE.toString() -> {
                model.getRandomArticleObs(view.getRxLifecycle())
                        .subscribe({
                            bean = it
                            view.showPage(model.getHtmlBody(it))
                        }, {
                            view.showLoadError()
                        })
            }
        }
    }

    override fun onBgChange() {
        bean?.let {
            view.showPage(model.getHtmlBody(bean))
        }
    }


}