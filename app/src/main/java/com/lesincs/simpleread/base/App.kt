package com.lesincs.simpleread.base

import android.app.Application
import android.content.Context
import com.lesincs.simpleread.dao.DaoMaster
import com.lesincs.simpleread.dao.DaoSession


/**
 * Created by Administrator on 2017/12/17.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        sContext = applicationContext

        /*子线程初始化GreenDao*/
        Thread{
            val helper = DaoMaster.DevOpenHelper(this, "simpleread.db") //获取helper对象
            val db = helper.writableDb // 获得db对象
            val daoMaster = DaoMaster(db) //获得daoMaster对象
            sDaoSession = daoMaster.newSession()//获得session对象,赋值给静态变量,方便调取
        }.start()

    }

    companion object {

        lateinit var sContext: Context

        lateinit var sDaoSession:DaoSession

    }

}