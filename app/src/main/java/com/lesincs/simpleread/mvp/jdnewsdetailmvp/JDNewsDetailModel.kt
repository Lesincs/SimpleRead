package com.lesincs.simpleread.mvp.jdnewsdetailmvp

import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.DBJDNews
import com.lesincs.simpleread.bean.JDNewsDetailBean
import com.lesincs.simpleread.dao.DBJDNewsDaoUtil
import com.lesincs.simpleread.retrofit.RetrofitManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2018/4/9.
 */
object JDNewsDetailModel : JDNewsDetailContract.Model {

    override fun getHtmlContent(baseContent: String): String {
        val sb = StringBuilder()
        val css = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/jd_fresh_news_light.css\"\n/>"
        sb.append("<!DOCTYPE html>")
        sb.append("<html dir=\"ltr\" lang=\"zh\">")
        sb.append("<head>")
        sb.append("<meta name=\"viewport\" content=\"width=100%; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\" />")
        sb.append(css)
        sb.append("</head>")
        sb.append("<body style=\"padding:0px 8px 8px 8px;\">")
        sb.append("<div id=\"pagewrapper\">")
        sb.append("<div id=\"mainwrapper\" class=\"clearfix\">")
        sb.append("<div id=\"maincontent\">")
        sb.append("<div class=\"post\">")
        sb.append("<div class=\"posthit\">")
        sb.append("<div class=\"entry\">")
        sb.append(baseContent)
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</body>")
        sb.append("</html>")
        return sb.toString()
    }

    override fun saveToDB(pageDetailBean: JDNewsDetailBean, newTitle: String, imageUrl: String, newsUrl: String, authorName: String, postDate: String) {
        if (DBJDNewsDaoUtil.isNewsInDB(pageDetailBean.post.id)) {

            if (!DBJDNewsDaoUtil.isNewsRead(pageDetailBean.post.id)) {
                DBJDNewsDaoUtil.markRead(pageDetailBean.post.id)
            }

        } else {
            val dbJDNews = DBJDNews()
            dbJDNews.isCollected = false
            dbJDNews.collectTime = System.currentTimeMillis()
            dbJDNews.newsId = pageDetailBean.post.id
            dbJDNews.isRead = true
            dbJDNews.imageUrl = imageUrl
            dbJDNews.newsTitle = newTitle
            dbJDNews.newsUrl = newsUrl
            dbJDNews.postDate = postDate
            dbJDNews.authorName = authorName
            DBJDNewsDaoUtil.insertToDB(dbJDNews)
        }
    }

    override fun getJDNewsDetailObs(newsId: String, rxLifecycle: RxLifecycle): Observable<JDNewsDetailBean> {
        return RetrofitManager.getJDService().getNewsDetailObs(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }

}