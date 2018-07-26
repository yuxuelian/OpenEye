package com.base.openeye.mvp.contract

import com.base.openeye.base.BasePresenter
import com.base.openeye.base.BaseView
import com.base.openeye.mvp.model.bean.HotBean

/**
 * Created by lvruheng on 2017/7/5.
 */
interface FindDetailContract {
    interface View : BaseView<Presenter> {
        fun setData(bean: HotBean)
    }

    interface Presenter : BasePresenter {
        fun requestData(categoryName: String, strategy: String)
    }
}