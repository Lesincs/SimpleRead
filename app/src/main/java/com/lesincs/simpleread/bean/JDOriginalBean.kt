package com.lesincs.simpleread.bean

/**
 * Created by Administrator on 2018/1/23.
 */
/*请求JD的新闻列表返回的数据对应的主类*/
data class JDNewsPrevBean(
		val status: String,
		val count: Int,
		val count_total: Int,
		val pages: Int, /*当前页面*/
		val posts: List<Post> /*post列表*/
)

data class Post(  /*post,即你看到的每个新鲜事列表条目的信息*/
		val id: String, /*每个新闻的id*/
		val url: String,/*每个新闻的网页链接*/
		val title: String,/*新闻标题*/
		val date: String,/*发布时间*/
		val tags: List<Tag>,
		val author: Author,/*新闻作者*/
		val comment_count: Int, /*评论数*/
		val custom_fields: CustomFields /*关联的图片,取数组的第一个*/
)

data class CustomFields(
		val thumb_c: List<String>
)

data class Tag(
		val id: String,
		val slug: String,
		val title: String,
		val description: String,
		val post_count: Int
)

data class Author(
		val id: String,
		val slug: String,
		val name: String,
		val first_name: String,
		val last_name: String,
		val nickname: String,
		val url: String,
		val description: String
)


/*新闻详情返回的数据类*/
data class JDNewsDetailBean(
		val status: String,
		val post: PostDetail,
		val previous_url: String
)

data class PostDetail(
		val id: String, /*新闻对应的id*/
		val content: String /*这个也是html数据,还需要进一步拼接*/
)