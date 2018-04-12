package com.lesincs.simpleread.bean

/**
 * Created by Administrator on 2017/12/15.
 */

//知乎日报新闻列表返回的数据对应的实体类
data class ZHNewsPrevBean(
		val date: String, /*发布时间*/
		val stories: List<Story>,/*一个story表示一个新闻*/
		val top_stories: List<TopStory> /*topstory就是banner展示的新闻,数据结构和story差不多*/
)

data class TopStory(
		val image: String,
		val type: Int,
		val id: String,
		val ga_prefix: String,
		val title: String
)

data class Story(
		val images: List<String>,
		val type: Int,
		val id: String,
		val ga_prefix: String,
		val title: String
)

/*知乎日报新闻详情返回的数据对应的实体类*/
data class ZHNewsDetailBean(
		val body: String,/*body为新闻html数据*/
		val image_source: String,/*首页展示的图片的来源*/
		val title: String,/*新闻标题*/
		val image: String,/*首页图片的url*/
		val share_url: String,
		val js: List<Any>,
		val ga_prefix: String,
		val images: List<String>,
		val type: Int,
		val id: String,/*新闻的id*/
		val css: List<String>
)





