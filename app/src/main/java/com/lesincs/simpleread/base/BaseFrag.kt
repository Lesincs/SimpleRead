package com.lesincs.simpleread.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Administrator on 2017/9/2.
 */
abstract class BaseFrag : Fragment() {

    /*免去每次新建frag都要加载view*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(getLayoutId(),container,false)
    }

    abstract fun getLayoutId(): Int
}