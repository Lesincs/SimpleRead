package com.lesincs.simpleread.base

import android.os.Bundle
import android.view.View

/**
 * Created by Administrator on 2017/10/17.
 */
abstract class LazyInitFragment : BaseFrag() {

    private var isPrepareView = false /*是够已经加载数据*/
    private var isVisibleToUser = false /*该fragment当前是否对用户可见*/
    private var isInitData = false /*是否已经加载过数据*/

    abstract override fun getLayoutId(): Int

    abstract fun loadData() //加载数据的方法

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepareView = true
    }

    /*在fragment由可见到不可见以及不可见到可见时回调*/
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        lazyInitData()
    }

    /*懒加载方法*/
    private fun lazyInitData() {
        if (isPrepareView && this.isVisibleToUser && !isInitData) {
            isInitData = true
            loadData()
        }
    }

    /*在该方法调用一次懒加载方法,避免第一次对用户可见的fragment不加载*/
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lazyInitData()
    }
}