package com.base.openeye.mvp.model

import android.content.Context
import com.base.openeye.mvp.contract.FindContract
import com.base.openeye.mvp.model.bean.FindBean
import com.base.openeye.mvp.model.bean.HomeBean
import com.base.openeye.network.ApiService
import com.base.openeye.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/6.
 */
class FindModel {
    fun loadData(): Observable<MutableList<FindBean>>{
        val apiService = RetrofitClient.create(ApiService::class.java)
        return apiService.getFindData()
    }
}