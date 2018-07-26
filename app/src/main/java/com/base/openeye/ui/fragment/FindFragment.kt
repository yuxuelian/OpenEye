package com.base.openeye.ui.fragment

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.base.openeye.R
import com.base.openeye.mvp.contract.FindContract
import com.base.openeye.mvp.model.bean.FindBean
import com.base.openeye.mvp.presenter.FindPresenter
import com.base.openeye.ui.FindDetailActivity
import com.base.openeye.utils.dip2px
import com.base.openeye.utils.defaultLoadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dinuscxj.itemdecoration.GridOffsetsItemDecoration
import kotlinx.android.synthetic.main.find_fragment.*

/**
 * Created by lvruheng on 2017/7/4.
 */
class FindFragment : BaseFragment(), FindContract.View {

    private lateinit var mPresenter: FindPresenter

    private val baseQuickAdapter: BaseQuickAdapter<FindBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<FindBean, BaseViewHolder>(R.layout.item_find) {
            override fun convert(helper: BaseViewHolder, item: FindBean) {
                val tvTitle: TextView = helper.getView(R.id.tv_title)
                val ivPhoto: ImageView = helper.getView(R.id.iv_photo)

                ivPhoto.defaultLoadImage(item.bgPicture)
                tvTitle.text = item.name ?: ""
            }
        }
    }

    override fun setData(beans: MutableList<FindBean>) {
        baseQuickAdapter.setNewData(beans)
    }

    override fun getLayoutResources(): Int {
        return R.layout.find_fragment
    }

    override fun initView() {
        mPresenter = FindPresenter(context!!, this)
        mPresenter.start()

        with(findRecyclerView) {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = baseQuickAdapter
            addItemDecoration(GridOffsetsItemDecoration(GridOffsetsItemDecoration.GRID_OFFSETS_VERTICAL).let {
                with(it) {
                    setVerticalItemOffsets(context.dip2px(10f).toInt())
                    setHorizontalItemOffsets(context.dip2px(10f).toInt())
                }
                it
            })
        }

        baseQuickAdapter.setOnItemClickListener { adapter, view, position ->
            val findBean = baseQuickAdapter.data[position]
            val intent = Intent(context, FindDetailActivity::class.java)
            intent.putExtra("name", findBean.name)
            startActivity(intent)
        }
    }
}