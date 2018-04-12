package com.lesincs.simpleread.dao

import com.lesincs.simpleread.base.App
import com.lesincs.simpleread.bean.DBZHNews

/**
 * Created by Administrator on 2018/4/8.
 */
object DBZHNewsDaoUtil {/*自己写的dao操作工具类*/
   private val sDBZHNewsDao = App.sDaoSession.dbzhNewsDao

    /*改变收藏状态*/
    fun changeCollectionState(newsId: String, isCollected: Boolean) {
        val dbZHNews = sDBZHNewsDao.queryBuilder().where(DBZHNewsDao.Properties.NewsId.eq(newsId)).build().list()[0]
        dbZHNews.isCollected = isCollected
        sDBZHNewsDao.update(dbZHNews)
    }

    /*标记已读*/
    fun markRead(newsId: String) {
        val dbZHNews = sDBZHNewsDao.queryBuilder().where(DBZHNewsDao.Properties.NewsId.eq(newsId)).build().list()[0]
        dbZHNews.isRead = true
        sDBZHNewsDao.update(dbZHNews)
    }

    /*判断是否已经存库*/
    fun isNewsInDB(newsId: String): Boolean {
        return sDBZHNewsDao.queryBuilder().where(DBZHNewsDao.Properties.NewsId.eq(newsId)).build().list().isNotEmpty()
    }

    /*增加到数据库*/
    fun insertToDB(dbZHNews:DBZHNews){
        sDBZHNewsDao.insert(dbZHNews)
    }

    /*判断是否已读*/
    fun isNewsRead(newsId: String):Boolean{
        val dbZHNewsList= sDBZHNewsDao.queryBuilder().where(DBZHNewsDao.Properties.NewsId.eq(newsId)).build().list()
        return dbZHNewsList.isNotEmpty() && dbZHNewsList[0].isRead
    }

    /*得到收藏列表*/
    fun getCollectionNewsList():List<DBZHNews>{
        return sDBZHNewsDao.queryBuilder().where(DBZHNewsDao.Properties.IsCollected.eq(true)).list()
    }

    /*判断是否收藏*/
    fun isNewsCollected(newsId: String):Boolean{
        val dbZHNewsList = sDBZHNewsDao.queryBuilder().where(DBZHNewsDao.Properties.NewsId.eq(newsId)).build().list()
        return dbZHNewsList.isNotEmpty()&&dbZHNewsList[0].isCollected
    }
}