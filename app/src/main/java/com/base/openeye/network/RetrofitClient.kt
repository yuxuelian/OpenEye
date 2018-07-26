package com.base.openeye.network

import com.base.openeye.App
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by haha on 2017/7/4.
 */
object RetrofitClient {

    private const val DEFAULT_TIMEOUT = 10000L

    /**
     * okHttpClient
     */
    private val okHttpClient: OkHttpClient by lazy {
        //缓存地址
        val httpCacheDirectory = File(App.instance().cacheDir, "AppCache")
        val cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)

        //okhttp创建了
        OkHttpClient
                .Builder()
                .cache(cache)
                .addInterceptor(CacheInterceptor())
                .addNetworkInterceptor(CacheInterceptor())
                .readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    /**
     * 默认的Retrofit
     */
    private val defaultRetrofit by lazy {
        createRetrofit(ApiService.BASE_URL)
    }

    private var retrofit = defaultRetrofit

    /**
     * 创建Retrofit
     */
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }

    fun resetBaseUrl() {
        retrofit = defaultRetrofit
    }

    fun changeBaseUrl(baseUrl: String) {
        retrofit = createRetrofit(baseUrl)
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}