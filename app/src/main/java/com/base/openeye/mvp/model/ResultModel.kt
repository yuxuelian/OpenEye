package com.base.openeye.mvp.model

import android.content.Context
import com.base.openeye.mvp.model.bean.HotBean
import com.base.openeye.network.ApiService
import com.base.openeye.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/11.
 */
class ResultModel {
    fun loadData(query: String, start: Int): Observable<HotBean> {
        val apiService = RetrofitClient.create(ApiService::class.java)
        return apiService.getSearchData(10, query, start)
    }
}
