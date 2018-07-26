package com.base.openeye.mvp.model

import android.content.Context
import android.util.Log
import com.base.openeye.mvp.model.bean.HomeBean
import com.base.openeye.mvp.model.bean.HotBean
import com.base.openeye.network.ApiService
import com.base.openeye.network.RetrofitClient
import io.reactivex.Observable
import java.net.URLEncoder

/**
 * Created by lvruheng on 2017/7/7.
 */
class FindDetailModel {
    fun loadData( categoryName: String, strategy: String?): Observable<HotBean>{
        val apiService = RetrofitClient.create(ApiService::class.java)
        return apiService.getFindDetailData(categoryName, strategy!!, "26868b32e808498db32fd51fb422d00175e179df", 83)
    }

    fun loadMoreData(start : Int, categoryName: String, strategy: String?): Observable<HotBean> {
        val apiService = RetrofitClient.create(ApiService::class.java)
        return apiService.getFindDetailMoreData(start,10,categoryName, strategy!!)
    }
}