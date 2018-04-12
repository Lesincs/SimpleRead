package com.lesincs.simpleread.bean

/**
 * Created by Administrator on 2017/12/27.
 */

/*每日一文的数据类 很简单*/
data class DailyArticleBean(
        val author: String, /*作者名*/
        val title: String,/*标题*/
        val content: String /*html内容,不完整,还需要在model层拼接*/
)


