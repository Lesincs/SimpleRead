package com.lesincs.simpleread.mvp.jdnewsdetailmvp

import android.view.MenuItem
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.JDNewsDetailBean
import io.reactivex.Observable

/**
 * Created by Administrator on 2018/1/23.
 */
interface JDNewsDetailContract {

    interface View {
        fun snackCollectSuccess()
        fun snackCancelCollection()
        fun snackCopySuccess()
        fun showExtraNewsTitle()
        fun showExtraAuthorNameAndPostDate()
        fun loadHtmlContent(htmlContent:String)
        fun loadExtraImageUrl()
        fun snackLoadError()
        fun getRxLifecycle(): RxLifecycle
    }

    interface Model {
        fun getJDNewsDetailObs(newsId: String, rxLifecycle: RxLifecycle): Observable<JDNewsDetailBean>
        fun saveToDB(pageDetailBean: JDNewsDetailBean, newTitle: String, imageUrl: String, newsUrl: String, authorName: String, postDate: String)
        fun getHtmlContent(baseContent: String):String
    }

    interface Presenter {
        fun onStart(newsId: String, newTitle: String, imageUrl: String, newsUrl: String, postDate: String, authorName: String)
        fun onShare(url: String, title: String)
        fun onCollected(pageId: String, item: MenuItem)
        fun onCopyLink(url: String)

    }

}