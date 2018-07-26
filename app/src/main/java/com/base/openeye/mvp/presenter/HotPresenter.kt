package com.base.openeye.mvp.presenter

import android.content.Context
import com.base.openeye.mvp.contract.HotContract
import com.base.openeye.mvp.model.HotModel
import com.base.openeye.mvp.model.bean.HotBean
import com.base.openeye.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/7.
 */
class HotPresenter(val mContext: Context, val mView: HotContract.View) : HotContract.Presenter {

    val mModel: HotModel by lazy {
        HotModel()
    }

    override fun start() {

    }

    override fun requestData(strategy: String) {
        mModel
                .loadData(strategy)
                .applySchedulers()
                .subscribe { bean: HotBean ->
                    mView?.setData(bean)
                }
    }

}