package com.lesincs.simpleread.mvp.jdnewsdetailmvp

import android.content.Intent
import android.view.MenuItem
import com.lesincs.simpleread.base.App
import com.lesincs.simpleread.bean.JDNewsDetailBean
import com.lesincs.simpleread.dao.DBJDNewsDaoUtil
import com.lesincs.simpleread.util.ClipboardUtil

/**
 * Created by Administrator on 2018/1/23.
 */
class JDNewsDetailPresenter(val view: JDNewsDetailContract.View) : JDNewsDetailContract.Presenter {

    private val model = JDNewsDetailModel
    var jdNewsDetailBean: JDNewsDetailBean? = null

    override fun onStart(newsId: String, newTitle: String, imageUrl: String, newsUrl: String, postDate: String, authorName: String) {
        view.showExtraNewsTitle()
        view.showExtraAuthorNameAndPostDate()
        view.loadExtraImageUrl()
        model.getJDNewsDetailObs(newsId, view.getRxLifecycle())
                .subscribe({
                    jdNewsDetailBean = it
                    view.loadHtmlContent(model.getHtmlContent(it.post.content))
                    model.saveToDB(it, newTitle, imageUrl, newsUrl, postDate, authorName)
                }, {
                    view.snackLoadError()
                })
    }


    override fun onShare(url: String, title: String) {
        jdNewsDetailBean?.let {
            val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, title + "\n" + url + "\n" + "from 简阅~")
            App.sContext.startActivity(Intent.createChooser(intent, "分享到"))
        }
    }

    override fun onCollected(pageId: String, item: MenuItem) {
        if (!item.isChecked) {
            item.isChecked = true
            view.snackCollectSuccess()
            //收藏逻辑
            DBJDNewsDaoUtil.changeCollectionState(pageId, true)
        } else {
            item.isChecked = false
            view.snackCancelCollection()
            //取消收藏逻辑
            DBJDNewsDaoUtil.changeCollectionState(pageId, false)
        }
    }

    override fun onCopyLink(url: String) {
        jdNewsDetailBean?.let {
            ClipboardUtil.copyTextToClipboard(url)
            view.snackCopySuccess()
        }
    }


}