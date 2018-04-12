package com.lesincs.simpleread.mvp.dailyarticlemvp

import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.DailyArticleBean
import io.reactivex.Observable

/**
 * Created by Administrator on 2017/12/27.
 */
interface DailyArticleContract {

    interface View {
        fun showPage(htmlBody: String)
        fun showLoadError()
        fun getRxLifecycle(): RxLifecycle
    }

    interface Model {
        fun getTodayArticleObs(rxLifecycle: RxLifecycle): Observable<DailyArticleBean>
        fun getRandomArticleObs(rxLifecycle: RxLifecycle): Observable<DailyArticleBean>
        fun getHtmlBody(dailyArticleBean: DailyArticleBean): String
    }

    interface Presenter {
        fun onStart(pageType: String)
        fun onBgChange()
    }
}