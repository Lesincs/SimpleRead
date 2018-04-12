package com.lesincs.simpleread.bean

/**
 * Created by Administrator on 2017/12/24.
 */

//这个是自己写的类，方便进行列表展示,知乎的api返回的类不是很好展示
data class NewsPrevItem(
        val id:String,
        val imageUrl:String,
        val title:String,
        val date:String
)