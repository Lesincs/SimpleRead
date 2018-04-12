package com.lesincs.simpleread.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Administrator on 2018/1/24.
 */
class FragAdapterWithTableLayout(fm:FragmentManager, private val frags:List<Fragment>, private val titles:List<String>):FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return frags[position]
    }
    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}