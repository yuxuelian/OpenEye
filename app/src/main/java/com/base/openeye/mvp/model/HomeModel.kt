package com.base.openeye.mvp.model

import android.content.Context
import com.base.openeye.mvp.model.bean.HomeBean
import com.base.openeye.network.ApiService
import com.base.openeye.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/5.
 */
class HomeModel {
    fun loadData(isFirst: Boolean, data: String?): Observable<HomeBean>{
        val apiService = RetrofitClient.create(ApiService::class.java)
        return if (isFirst) {
            apiService.getHomeData()
        } else {
            apiService.getHomeMoreData(data.toString(), "2")
        }
    }
}