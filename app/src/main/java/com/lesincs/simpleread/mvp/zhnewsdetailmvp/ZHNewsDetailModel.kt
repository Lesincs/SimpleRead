package com.lesincs.simpleread.mvp.zhnewsdetailmvp

import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.DBZHNews
import com.lesincs.simpleread.bean.ZHNewsDetailBean
import com.lesincs.simpleread.dao.DBZHNewsDaoUtil
import com.lesincs.simpleread.retrofit.RetrofitManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2018/4/8.
 */
object ZHNewsDetailModel : ZHNewsDetailContract.Model {

    override fun getHtmlContent(htmlBody: String): String {
        val sb = StringBuilder()
        val css = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/zhihu_light.css\"\n/>"
        sb.append("<html>")
                .append("<head>")
                .append(css)
                .append("</head>")
                .append(htmlBody)
                .append("</html>")
        return sb.toString()
    }

    override fun saveToDB(newsId: String, imageUrl: String, newsTitle: String) {
        if (DBZHNewsDaoUtil.isNewsInDB(newsId)) {
            if (!DBZHNewsDaoUtil.isNewsRead(newsId)) {
                DBZHNewsDaoUtil.markRead(newsId)
            }
        } else {
            val dbZHNews = DBZHNews()
            dbZHNews.isCollected = false
            dbZHNews.collectTime = System.currentTimeMillis()
            dbZHNews.newsId = newsId
            dbZHNews.isRead = true
            dbZHNews.imageUrl = imageUrl
            dbZHNews.newsTitle = newsTitle
            DBZHNewsDaoUtil.insertToDB(dbZHNews)
        }
    }

    override fun getNewsDetailObs(newsId: String, rxLifecycle: RxLifecycle): Observable<ZHNewsDetailBean> {
        return RetrofitManager.getZhiHuService().getNewsDetailObs(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }

}