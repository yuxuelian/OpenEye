package com.base.openeye.mvp.contract

import com.base.openeye.base.BasePresenter
import com.base.openeye.base.BaseView
import com.base.openeye.mvp.model.bean.FindBean

/**
 * Created by lvruheng on 2017/7/6.
 */
interface FindContract{
    interface View : BaseView<Presenter> {
        fun setData(beans : MutableList<FindBean>)
    }
    interface Presenter : BasePresenter {
        fun requestData()
    }
}