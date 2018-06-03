package com.lesincs.simpleread.custom

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.lesincs.simpleread.R
import com.lesincs.simpleread.bean.TopStory
import com.lesincs.simpleread.ui.adapter.BannerAdapter
import com.lesincs.simpleread.ui.fragment.ZHNewsListFrag
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_banner.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017/12/23.
 */
class CustomViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    private var mDisposable: Disposable? = null
    private var topStories = ArrayList<TopStory>()
    private  var  lastx:Float = 0f
    fun update(topStories: List<TopStory>) {
        if (!this.topStories.containsAll(topStories)) {
            this.topStories.clear()
            this.topStories.addAll(topStories)
            adapter?.notifyDataSetChanged()
        }
    }


    fun init(frag: ZHNewsListFrag) {

        adapter = BannerAdapter(this.topStories, context)

        RxLifecycleCompact.bind(frag).toObservable()
                .subscribe {
                    when (it) {
                        LifecycleEvent.RESUME -> {
                            mDisposable?.dispose()
                            mDisposable = startInterval()
                        }
                        LifecycleEvent.DESTROY_VIEW -> {
                            mDisposable?.dispose()
                        }
                        LifecycleEvent.PAUSE -> {
                            mDisposable?.dispose()
                        }
                    }
                }
    }


    fun startInterval(): Disposable? {
        mDisposable = Observable.interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (currentItem == 4) {
                        currentItem = 0
                    } else {
                        currentItem++
                    }
                })
        return mDisposable
    }

}