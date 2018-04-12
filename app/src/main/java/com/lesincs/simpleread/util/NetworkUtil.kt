package com.lesincs.simpleread.util

import android.content.Context
import android.net.ConnectivityManager
import com.lesincs.simpleread.base.App

/**
 * Created by Administrator on 2017/12/17.
 */
class NetworkUtil {

        companion object {
            fun isNetWorkAvailable(): Boolean {
                val manager = App.sContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return when (manager.activeNetworkInfo) {
                    null -> false
                    else -> manager.activeNetworkInfo.isAvailable
                }
            }
        }
}