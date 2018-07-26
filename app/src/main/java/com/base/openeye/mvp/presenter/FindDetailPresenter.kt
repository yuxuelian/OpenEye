package com.base.openeye.mvp.presenter

import android.content.Context
import com.base.openeye.mvp.contract.FindDetailContract
import com.base.openeye.mvp.model.FindDetailModel
import com.base.openeye.mvp.model.bean.HotBean
import com.base.openeye.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by lvruheng on 2017/7/7.
 */
class FindDetailPresenter(val mContext: Context, val mView: FindDetailContract.View) : FindDetailContract.Presenter {

    val mModel: FindDetailModel by lazy {
        FindDetailModel()
    }

    override fun start() {

    }

    override fun requestData(categoryName: String, strategy: String) {
        val observable: Observable<HotBean>? = mContext?.let { mModel.loadData(categoryName, strategy) }
        observable?.applySchedulers()?.subscribe { bean: HotBean ->
            mView?.setData(bean)
        }
    }

    fun requesMoreData(start: Int, categoryName: String, strategy: String) {
        val observable: Observable<HotBean>? = mContext?.let { mModel.loadMoreData(start, categoryName, strategy) }
        observable?.applySchedulers()?.subscribe { bean: HotBean ->
            mView?.setData(bean)
        }
    }

}