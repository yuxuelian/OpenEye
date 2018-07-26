package com.base.openeye.mvp.presenter

import android.content.Context
import com.base.openeye.mvp.contract.HomeContract
import com.base.openeye.mvp.model.HomeModel
import com.base.openeye.mvp.model.bean.HomeBean
import com.base.openeye.utils.applySchedulers
import io.reactivex.Observable


/**
 * Created by lvruheng on 2017/7/5.
 */
class HomePresenter(private val mContext: Context, private val mView: HomeContract.View) : HomeContract.Presenter {
    private val mModel: HomeModel by lazy {
        HomeModel()
    }


    override fun start() {
        requestData()
    }

    override fun requestData() {
        val observable: Observable<HomeBean>? = mContext.let { mModel.loadData(true, "0") }
        observable?.applySchedulers()?.subscribe { homeBean: HomeBean ->
            mView.setData(homeBean)
        }
    }

    fun moreData(data: String?) {
        val observable: Observable<HomeBean>? = mContext.let { mModel.loadData(false, data) }
        observable?.applySchedulers()?.subscribe { homeBean: HomeBean ->
            mView?.setData(homeBean)
        }
    }
}




