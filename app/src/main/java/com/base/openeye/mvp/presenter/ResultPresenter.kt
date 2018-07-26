package com.base.openeye.mvp.presenter

import android.content.Context
import com.base.openeye.mvp.contract.FindContract
import com.base.openeye.mvp.contract.HotContract
import com.base.openeye.mvp.contract.ResultContract
import com.base.openeye.mvp.model.FindModel
import com.base.openeye.mvp.model.HotModel
import com.base.openeye.mvp.model.ResultModel
import com.base.openeye.mvp.model.bean.FindBean
import com.base.openeye.mvp.model.bean.HotBean
import com.base.openeye.utils.applySchedulers
import io.reactivex.BackpressureOverflowStrategy
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/7.
 */
class ResultPresenter(val mContext: Context, val mView: ResultContract.View) : ResultContract.Presenter {

    val mModel: ResultModel by lazy {
        ResultModel()
    }

    override fun start() {

    }

    override fun requestData(query: String, start: Int) {
        mModel.loadData(query, start)
                .applySchedulers()
                .subscribe { bean: HotBean ->
                    mView.setData(bean)
                }
    }
}