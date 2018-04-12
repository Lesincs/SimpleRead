package com.lesincs.simpleread.dao

import com.lesincs.simpleread.base.App
import com.lesincs.simpleread.bean.DBJDNews

/**
 * Created by Administrator on 2018/4/8.
 */
object DBJDNewsDaoUtil { /*自己写的dao操作工具类*/

   private val sDBJDNewsDao = App.sDaoSession.dbjdNewsDao

    /*改变收藏状态*/
    fun changeCollectionState(newsId: String, isCollected: Boolean) {
        val dbJDNews = sDBJDNewsDao.queryBuilder().where(DBJDNewsDao.Properties.NewsId.eq(newsId)).build().list()[0]
        dbJDNews.isCollected = isCollected
        sDBJDNewsDao.update(dbJDNews)
    }

    /*标记已读*/
    fun markRead(newsId: String) {
        val dbJDNews = sDBJDNewsDao.queryBuilder().where(DBJDNewsDao.Properties.NewsId.eq(newsId)).build().list()[0]
        dbJDNews.isRead = true
        sDBJDNewsDao.update(dbJDNews)
    }

    /*判断是否已经存库*/
    fun isNewsInDB(newsId: String): Boolean {
        return sDBJDNewsDao.queryBuilder().where(DBJDNewsDao.Properties.NewsId.eq(newsId)).build().list().isNotEmpty()
    }

    /*增加到数据库*/
    fun insertToDB(dbJDNews:DBJDNews){
        sDBJDNewsDao.insert(dbJDNews)
    }

    /*判断是否已读*/
    fun isNewsRead(newsId: String):Boolean{
        val dbJDNewsList = sDBJDNewsDao.queryBuilder().where(DBJDNewsDao.Properties.NewsId.eq(newsId)).build().list()

        return dbJDNewsList.isNotEmpty()&&dbJDNewsList[0].isRead
    }

    /*得到收藏列表*/
    fun getCollectionNewsList():List<DBJDNews>{
        return sDBJDNewsDao.queryBuilder().where(DBJDNewsDao.Properties.IsCollected.eq(true)).list()
    }

    /*判断是否收藏*/
    fun isNewsCollected(newsId: String):Boolean{
        val dbJDNewsList = sDBJDNewsDao.queryBuilder().where(DBJDNewsDao.Properties.NewsId.eq(newsId)).build().list()
        return dbJDNewsList.isNotEmpty() && dbJDNewsList[0].isCollected
    }
}