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

object RetrofitManager {

    private val ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/4/news/"
    private val JD_FRESH_NEWS_BASE_URL = "http://i.jandan.net/"
    private val CACHE_MAX_SIZE = 100 * 1024 * 1024L
    private val okHttpClient: OkHttpClient by lazy { createOkHttpClient() }
    private val zhRetrofit: Retrofit by lazy { createZHRetrofit() }
    private val jdRetrofit: Retrofit by lazy { createJDRetrofit() }
    private val iZhiHuService: IZhiHuService by lazy { zhRetrofit.create(IZhiHuService::class.java) }
    private val iJDService: IJDService by lazy { jdRetrofit.create(IJDService::class.java) }

    /*创建知乎retrofit实例*/
    private fun createZHRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(ZHIHU_BASE_URL)
                .build()
    }

    /*创建煎蛋retrofit实例*/
    private fun createJDRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(JD_FRESH_NEWS_BASE_URL).build()
    }

    /*创建一个带缓存的OkHttpClient*/
    private fun createOkHttpClient(): OkHttpClient {
        val cache = Cache(File(App.sContext.cacheDir, "OkHttpCache"), CACHE_MAX_SIZE)
        return OkHttpClient().newBuilder()
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    fun getZhiHuService(): IZhiHuService {
        return iZhiHuService
    }

    fun getJDService(): IJDService {
        return iJDService
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