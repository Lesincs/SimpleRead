package com.lesincs.simpleread.custom

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
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
    lateinit var llIndicatorNPF: LinearLayout

    fun update(topStories: List<TopStory>) {
        if (!this.topStories.containsAll(topStories)) {
            this.topStories.clear()
            this.topStories.addAll(topStories)
            adapter?.notifyDataSetChanged()
        }
    }


    fun init(llIndicatorNPF: LinearLayout, frag: ZHNewsListFrag) {

        adapter = BannerAdapter(this.topStories, context)
        addOnPageChangeListener(onPageChangeListener)
        this.llIndicatorNPF = llIndicatorNPF

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


    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageSelected(position: Int) {

            with(llIndicatorNPF) {
                when (position) {
                    0 -> {
                        indicator0.setBackgroundResource(R.drawable.indicator_choose)
                        indicator1.setBackgroundResource(R.drawable.indicator_not_choose)
                        indicator4.setBackgroundResource(R.drawable.indicator_not_choose)
                    }
                    1 -> {
                        indicator1.setBackgroundResource(R.drawable.indicator_choose)
                        indicator0.setBackgroundResource(R.drawable.indicator_not_choose)
                        indicator2.setBackgroundResource(R.drawable.indicator_not_choose)
                    }
                    2 -> {
                        indicator2.setBackgroundResource(R.drawable.indicator_choose)
                        indicator1.setBackgroundResource(R.drawable.indicator_not_choose)
                        indicator3.setBackgroundResource(R.drawable.indicator_not_choose)
                    }
                    3 -> {
                        indicator3.setBackgroundResource(R.drawable.indicator_choose)
                        indicator2.setBackgroundResource(R.drawable.indicator_not_choose)
                        indicator4.setBackgroundResource(R.drawable.indicator_not_choose)
                    }
                    4 -> {
                        indicator4.setBackgroundResource(R.drawable.indicator_choose)
                        indicator3.setBackgroundResource(R.drawable.indicator_not_choose)
                    }
                }
            }
        }
    }

}