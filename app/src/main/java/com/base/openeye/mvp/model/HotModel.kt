package com.base.openeye.mvp.model

import android.content.Context
import com.base.openeye.mvp.model.bean.HomeBean
import com.base.openeye.mvp.model.bean.HotBean
import com.base.openeye.network.ApiService
import com.base.openeye.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/7.
 */
class HotModel{
    fun loadData( strategy: String?): Observable<HotBean>{
        val apiService  = RetrofitClient.create(ApiService::class.java)
          return apiService.getHotData(10, strategy!!,"26868b32e808498db32fd51fb422d00175e179df",83)
        }
    }