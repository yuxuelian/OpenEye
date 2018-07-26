package com.base.openeye.mvp.presenter

import android.content.Context
import android.util.Log
import com.base.openeye.mvp.contract.FindContract
import com.base.openeye.mvp.contract.HomeContract
import com.base.openeye.mvp.model.FindModel
import com.base.openeye.mvp.model.HomeModel
import com.base.openeye.mvp.model.bean.FindBean
import com.base.openeye.mvp.model.bean.HomeBean
import com.base.openeye.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/6.
 */
class FindPresenter(val mContext: Context,val  mView: FindContract.View) : FindContract.Presenter {

    val mModel: FindModel by lazy {
        FindModel()
    }


    override fun start() {
        requestData()
    }

    override fun requestData() {
        val observable: Observable<MutableList<FindBean>>? = mContext?.let { mModel.loadData() }
        observable?.applySchedulers()?.subscribe { beans: MutableList<FindBean> ->
            mView?.setData(beans)
        }
    }


}