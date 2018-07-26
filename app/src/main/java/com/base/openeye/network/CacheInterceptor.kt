package com.base.openeye.network

import android.util.Log
import com.base.openeye.App
import com.base.openeye.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Created by lvruheng on 2017/7/4.
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        return if (NetworkUtils.isNetConneted(App.instance())) {
            val response = chain.proceed(request)

            // read from cache for 60 s
            val maxAge = 60

            val cacheControl = request?.cacheControl().toString()
            Log.e("CacheInterceptor", "6s load cahe" + cacheControl)

            response
                    .newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build()
        } else {
            Log.e("CacheInterceptor", " no network load cahe")
            request = request
                    .newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()

            val response = chain.proceed(request)

            //set cahe times is 3 days
            val maxStale = 60 * 60 * 24 * 3

            response
                    .newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build()
        }
    }
}