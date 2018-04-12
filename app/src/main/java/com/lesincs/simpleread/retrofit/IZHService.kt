package com.lesincs.simpleread.retrofit

import com.lesincs.simpleread.bean.ZHNewsDetailBean
import com.lesincs.simpleread.bean.ZHNewsPrevBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by Administrator on 2017/12/17.
 */


const val SHORT_CACHE_HEADER = "Cache-Control: public, max-age="
const val LATEST_NEWS_SHORT_CACHE_TIME = 60      //60s
const val BEFORE_DATE_NEWS_SHORT_CACHE_TIME = 60 * 60 * 24 * 7  //七天
const val PAGE_DETAIL_SHORT_CACHE_TIME = 60 * 60 * 24 * 7    //七天

interface IZhiHuService {

    @Headers("$SHORT_CACHE_HEADER$LATEST_NEWS_SHORT_CACHE_TIME")
    @GET("{latest}")
    fun getLatestNewsListObs(@Path("latest") latest: String = "latest"): Observable<ZHNewsPrevBean>

    @Headers("$SHORT_CACHE_HEADER$BEFORE_DATE_NEWS_SHORT_CACHE_TIME")
    @GET("before/{date}")
    fun getBeforeDateNewsListObs(@Path("date") date: String): Observable<ZHNewsPrevBean> //yyyyMMdd格式

    @Headers("$SHORT_CACHE_HEADER$PAGE_DETAIL_SHORT_CACHE_TIME")
    @GET("{newsId}")
    fun getNewsDetailObs(@Path("newsId") newsId: String): Observable<ZHNewsDetailBean>
}