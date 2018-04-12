package com.lesincs.simpleread.mvp.dailyarticlemvp

import android.util.Log
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.DailyArticleBean
import com.lesincs.simpleread.custom.EBgColor
import com.lesincs.simpleread.util.PrefUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup

/**
 * Created by Administrator on 2018/4/9.
 */
object DailyArticleModel : DailyArticleContract.Model {

    private val TODAY_ARTICLE_URL = "http://meiriyiwen.com"
    private val RANDOM_ARTICLE_URL = "http://meiriyiwen.com/random"

    override fun getTodayArticleObs(rxLifecycle: RxLifecycle): Observable<DailyArticleBean> {
        return Observable.fromCallable {
            //选取body文档
            val doc = Jsoup.connect(TODAY_ARTICLE_URL).get().body()
            //选择第一个“h1”标签
            val title = doc.selectFirst("h1").text()
            //选择类为”article_author“的所有标签取text
            val author = doc.getElementsByAttributeValue("class", "article_author").text()
            //选择类为”article_author“的所有标签取html,方便自己拼接css
            var body = doc.getElementsByAttributeValue("class", "article_text").removeAttr("div").html()
            val regex = Regex("<p>\\s+</p>")
            body = body.replace(regex, "")

            return@fromCallable DailyArticleBean(author, title, body)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }

    override fun getRandomArticleObs(rxLifecycle: RxLifecycle): Observable<DailyArticleBean> {
        return Observable.fromCallable {
            //选取body文档
            val doc = Jsoup.connect(RANDOM_ARTICLE_URL).get().body()
            //选择第一个“h1”标签
            val title = doc.selectFirst("h1").text()
            //选择类为”article_author“的所有标签取text
            val author = doc.getElementsByAttributeValue("class", "article_author").text()
            //选择类为”article_author“的所有标签取html,方便自己拼接css
            var body = doc.getElementsByAttributeValue("class", "article_text").removeAttr("div").html()
            /*替换莫名空段落*/
            val regex = Regex("<p>\\s+</p>")
            body = body.replace(regex, "")
            return@fromCallable DailyArticleBean(author, title, body)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }

    override fun getHtmlBody(dailyArticleBean: DailyArticleBean): String {
        val css = when (PrefUtil.getLastBgColor()) {
            EBgColor.WHITE -> "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/daily_article_white_bg.css\"/>\n"
            EBgColor.BLACK -> "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/daily_article_black_bg.css\"/>\n"
            EBgColor.BROWN -> "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/daily_article_brown_bg.css\"/>\n"
        }

        val sb = StringBuilder()
        sb.append("<html>")
                .append("<head>")
                .append(css)
                .append("</head>")
                .append("<body>")
                .append("<h1 class='title'>")
                .append(dailyArticleBean.title)
                .append("</h1>")
                .append("<h2 class='author'>")
                .append(dailyArticleBean.author)
                .append("</h2>")
                .append("<div class='content'>")
                .append(dailyArticleBean.content)
                .append("</div>")
                .append("</body>")
                .append("</html>")

        return sb.toString()
    }
}