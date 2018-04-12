package com.lesincs.simpleread.retrofit

import com.lesincs.simpleread.base.App
import com.lesincs.simpleread.util.NetworkUtil
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017/12/17.
 */

object RetrofitManager {

    private val ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/4/news/"
    private val DAILY_ARTICLE_BASE_URL = "https://interface.meiriyiwen.com/article/"
    private val JD_FRESHNEWS_BASE_URL = "http://i.jandan.net/"
    private val CACHE_STATE_NETWORK = 5
    private val CACHE_STATE_NO_NETWORK = 60 * 60 * 24 * 15
    private val CACHE_CONTROL_NO_NETWORK = "public, only-if-cached, max-stale=" + CACHE_STATE_NO_NETWORK
    private val CACHE_CONTROL_NETWORK = "max-age=" + CACHE_STATE_NETWORK
    private val CACHE_MAX_SIZE = 100 * 1024 * 1024L
    private var okHttpClient: OkHttpClient? = null
    private var zhRetrofit: Retrofit? = null
    private var jdRetrofit: Retrofit? = null
    private var iZhiHuService: IZhiHuService? = null
    private var iJDService: IJDService? = null

    /*返回知乎retrofit实例*/
    private fun getZHRetrofit(): Retrofit {
        if (zhRetrofit == null) {
            zhRetrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .baseUrl(ZHIHU_BASE_URL)
                    .build()
        }
        return zhRetrofit!!
    }

    /*返回煎蛋retrofit实例*/
    private fun getJDRetrofit(): Retrofit {
        if (jdRetrofit == null) {
            jdRetrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .baseUrl(JD_FRESHNEWS_BASE_URL)
                    .build()
        }
        return jdRetrofit!!
    }

    /*返回一个带缓存的OkHttpClient*/
    private fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            val cache = Cache(File(App.sContext.cacheDir, "OkHttpCache"), CACHE_MAX_SIZE)
            okHttpClient = OkHttpClient().newBuilder()
                    .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                    .addInterceptor(OFFLINE_INTERCEPTOR)
                    .cache(cache)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build()
        }
        return okHttpClient!!
    }

    /*保证只有一个Service实例*/
    fun getZhiHuService(): IZhiHuService {
        if (iZhiHuService == null) {
            iZhiHuService = getZHRetrofit().create(IZhiHuService::class.java)
        }
        return iZhiHuService!!
    }

    /*保证只有一个Service实例*/
    fun getJDService(): IJDService {
        if (iJDService == null) {
            iJDService = getJDRetrofit().create(IJDService::class.java)
        }
        return iJDService!!
    }

    /*有网拦截器 使用缓存的时候需要用到*/
    private val REWRITE_RESPONSE_INTERCEPTOR = Interceptor { chain ->

        val caches = chain.request().cacheControl().toString()
        val originalResponse = chain.proceed(chain.request())
        val cacheControl = originalResponse.header("Cache-Control")

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            originalResponse.newBuilder()
                    .header("Cache-Control", caches)
                    .build()
        } else {
            originalResponse
        }
    }
    /*无网拦截器 也是缓存的时候用到*/
    private val OFFLINE_INTERCEPTOR = Interceptor { chain ->

        var request = chain.request()

        if (!NetworkUtil.isNetWorkAvailable()) {
            val maxStale = 60 * 60 * 24 * 28
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build()
        }
        chain.proceed(request)
    }

}