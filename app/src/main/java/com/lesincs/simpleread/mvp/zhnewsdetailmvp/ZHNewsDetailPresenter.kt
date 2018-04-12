package com.lesincs.simpleread.mvp.zhnewsdetailmvp

import android.content.Intent
import android.view.MenuItem
import com.lesincs.simpleread.base.App
import com.lesincs.simpleread.bean.ZHNewsDetailBean
import com.lesincs.simpleread.dao.DBZHNewsDaoUtil
import com.lesincs.simpleread.util.ClipboardUtil

/**
 * Created by Administrator on 2017/12/24.
 */
class ZHNewsDetailPresenter(val view: ZHNewsDetailContract.View) : ZHNewsDetailContract.Presenter {

    private val model = ZHNewsDetailModel
    private var pageDetailBean: ZHNewsDetailBean? = null

    override fun onStart(pageId: String) {
        model.getNewsDetailObs(pageId, view.getRxLifecycle())
                .subscribe({
                    pageDetailBean = it
                    view.showNewsTitle(it.title)
                    view.showNewsImageSource(it.image_source)
                    view.loadImageUrl(it.image)
                    view.loadHtmlContent(model.getHtmlContent(it.body))
                    model.saveToDB(it.id, it.images[0], it.title)
                }, {
                    view.snackLoadError()
                })
    }

    override fun onShare() {
        pageDetailBean?.let {
            val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, it.title + "\n" + it.share_url + "\n" + "from 简阅~")
            App.sContext.startActivity(Intent.createChooser(intent, "分享到"))
        }
    }

    override fun onCollected(pageId: String, item: MenuItem) {
        if (!item.isChecked) {
            item.isChecked = true
            view.snackCollectSuccess()
            //收藏逻辑
            DBZHNewsDaoUtil.changeCollectionState(pageId, true)
        } else {
            item.isChecked = false
            view.snackCancelCollection()
            //取消收藏逻辑
            DBZHNewsDaoUtil.changeCollectionState(pageId, false)
        }
    }

    override fun onCopyLink() {
        pageDetailBean?.let {
            ClipboardUtil.copyTextToClipboard(it.share_url)
            view.snackCopySuccess()
        }
    }

}