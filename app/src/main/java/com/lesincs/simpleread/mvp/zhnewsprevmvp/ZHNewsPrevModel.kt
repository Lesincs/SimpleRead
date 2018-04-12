package com.lesincs.simpleread.mvp.zhnewsprevmvp

import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.NewsPrevItem
import com.lesincs.simpleread.bean.ZHNewsPrevBean
import com.lesincs.simpleread.retrofit.RetrofitManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2018/4/8.
 */
object ZHNewsPrevModel : ZHNewsPrevContract.Model {
    override fun getBeforeDateNewsListObs(sDate: String, rxLifecycle: RxLifecycle): Observable<ZHNewsPrevBean> {
        return RetrofitManager.getZhiHuService().getBeforeDateNewsListObs(sDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }

    override fun getLatestNewsListObs(rxLifecycle: RxLifecycle): Observable<ZHNewsPrevBean> {
        return RetrofitManager.getZhiHuService().getLatestNewsListObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }

    override fun getNewsListItemList(newsListBean: ZHNewsPrevBean): List<NewsPrevItem> {
        val sDate = newsListBean.date
        val newsListItems = ArrayList<NewsPrevItem>()
        newsListBean.stories.forEach {
            val imageURL = if (it.images == null) {
                ""
            } else {
                it.images[0]
            }
            val newListItem = NewsPrevItem(it.id, imageURL, it.title, sDate)
            newsListItems.add(newListItem)
        }
        return newsListItems
    }

}