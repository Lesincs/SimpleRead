package com.lesincs.simpleread.mvp.jdnewsprevmvp

import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.RxLifecycle
import com.lesincs.simpleread.bean.JDNewsPrevBean
import com.lesincs.simpleread.retrofit.RetrofitManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2018/4/9.
 */
object JDNewsPrevModel : JDNewsPrevContract.Model {

    override fun getLatestNewsObs(rxLifecycle: RxLifecycle): Observable<JDNewsPrevBean> {
        return RetrofitManager.getJDService().getLatestNewsObs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }

    override fun getBeforeNewsObs(page: Int, rxLifecycle: RxLifecycle): Observable<JDNewsPrevBean> {
        return RetrofitManager.getJDService().getBeforeNewsObs("get_recent_posts",
                "url,date,tags,author,title,comment_count,custom_fields",
                "%s",
                "thumb_c,views",
                1, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxLifecycle.disposeObservableWhen(LifecycleEvent.DESTROY))
    }
}