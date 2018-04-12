package com.lesincs.simpleread.retrofit

import com.lesincs.simpleread.bean.JDNewsDetailBean
import com.lesincs.simpleread.bean.JDNewsPrevBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * Created by Administrator on 2018/1/23.
 */


interface IJDService {

    @Headers("$SHORT_CACHE_HEADER$LATEST_NEWS_SHORT_CACHE_TIME")
    @GET("/")
    fun getLatestNewsObs(@Query("oxwlxojflwblxbsapi") p1: String = "get_recent_posts",
                         @Query("include") p2: String = "url,date,tags,author,title,comment_count,custom_fields",
                         @Query("page") p3: String = "%s",
                         @Query("custom_fields") p4: String = "thumb_c,views",
                         @Query("dev") p5: Int = 1,
                         @Query("page") p6: Int = 1): Observable<JDNewsPrevBean>

    @GET("/")
    fun getBeforeNewsObs(@Query("oxwlxojflwblxbsapi") p1: String = "get_recent_posts",
                         @Query("include") p2: String = "url,date,tags,author,title,comment_count,custom_fields",
                         @Query("page") p3: String = "%s",
                         @Query("custom_fields") p4: String = "thumb_c,views",
                         @Query("dev") p5: Int = 1,
                         @Query("page") p6: Int): Observable<JDNewsPrevBean>

    @Headers("$SHORT_CACHE_HEADER$PAGE_DETAIL_SHORT_CACHE_TIME")
    @GET("/")
    fun getNewsDetailObs(@Query("id") newsId: String,
                         @Query("oxwlxojflwblxbsapi") p1: String = "get_post",
                         @Query("include") p2: String = "content"): Observable<JDNewsDetailBean>
}