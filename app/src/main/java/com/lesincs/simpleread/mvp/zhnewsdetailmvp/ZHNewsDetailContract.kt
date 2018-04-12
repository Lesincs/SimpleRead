package com.lesincs.simpleread.mvp.zhnewsdetailmvp

import android.view.MenuItem
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.ZHNewsDetailBean
import io.reactivex.Observable

/**
 * Created by Administrator on 2017/12/24.
 */
interface ZHNewsDetailContract {

    interface Presenter {
        fun onStart(pageId: String)
        fun onShare()
        fun onCollected(pageId: String, item: MenuItem)
        fun onCopyLink()
    }

    interface Model {
        fun getNewsDetailObs(newsId: String, rxLifecycle: RxLifecycle): Observable<ZHNewsDetailBean>
        fun saveToDB(newsId: String, imageUrl: String, newsTitle: String)
        fun getHtmlContent(htmlBody: String): String
    }

    interface View {
        fun snackCollectSuccess()
        fun snackCancelCollection()
        fun snackCopySuccess()
        fun showNewsTitle(newsTitle: String)
        fun showNewsImageSource(imageSource: String)
        fun loadImageUrl(imageUrl: String)
        fun loadHtmlContent(htmlContent: String)
        fun snackLoadError()
        fun getRxLifecycle(): RxLifecycle
    }
}