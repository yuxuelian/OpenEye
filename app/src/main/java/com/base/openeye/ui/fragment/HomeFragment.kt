package com.base.openeye.ui.fragment

import android.content.Intent
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.base.openeye.App
import com.base.openeye.R
import com.base.openeye.mvp.contract.HomeContract
import com.base.openeye.mvp.model.bean.HomeBean
import com.base.openeye.mvp.model.bean.HomeBean.IssueListBean.ItemListBean
import com.base.openeye.mvp.model.bean.VideoBean
import com.base.openeye.mvp.presenter.HomePresenter
import com.base.openeye.ui.VideoDetailActivity
import com.base.openeye.utils.ObjectSaveUtils
import com.base.openeye.utils.SPUtils
import com.base.openeye.utils.defaultLoadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.home_fragment.*
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by lvruheng on 2017/7/4.
 */
class HomeFragment : BaseFragment(), HomeContract.View {
    private lateinit var mPresenter: HomePresenter

    private var mList = ArrayList<ItemListBean>()

    var dataFlag: String? = null

    private val baseQuickAdapter = object : BaseQuickAdapter<ItemListBean, BaseViewHolder>(R.layout.item_home, mList) {
        override fun convert(helper: BaseViewHolder, item: ItemListBean) {
            val title = item.data?.title
            val category = item.data?.category
            val minute = item.data?.duration?.div(60)
            val second = item.data?.duration?.minus((minute?.times(60)) as Long)

            val numFormat = DecimalFormat("00")

            val realMinute = numFormat.format(minute)
            val realSecond = numFormat.format(second)

            val photo = item.data?.cover?.feed

            val author = item.data?.author

            val ivPhoto = helper.getView<ImageView>(R.id.iv_photo)
            ivPhoto.defaultLoadImage(photo)
            val ivUser = helper.getView<ImageView>(R.id.iv_user)

            val tvTitle = helper.getView<TextView>(R.id.tv_title)
            tvTitle.typeface = App.fontFZLanTingHeiSDb1
            tvTitle.text = title

            val tvDetail = helper.getView<TextView>(R.id.tv_detail)
            tvDetail.text = "发布于 $category / $realMinute:$realSecond"

            if (author != null) {
                ivUser.defaultLoadImage(author.icon)
            } else {
                ivUser.visibility = View.GONE
            }
        }
    }

    override fun setData(bean: HomeBean) {
        val regEx = """[^0-9]"""
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean.nextPageUrl)

        dataFlag = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()

        if (refreshLayout.isRefreshing){
            refreshLayout.isRefreshing = false
            if (mList.size > 0) {
                mList.clear()
            }
        }

        if (baseQuickAdapter.isLoading) {
            baseQuickAdapter.loadMoreComplete()
        }

        bean.issueList!!
                .flatMap { it.itemList!! }
                .filter { it.type.equals("video") }
                .forEach { mList.add(it) }
        baseQuickAdapter.notifyDataSetChanged()
    }

    override fun getLayoutResources(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        mPresenter = HomePresenter(context!!, this)
        mPresenter.start()

        baseQuickAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = baseQuickAdapter.getItem(position)!!

            val title = bean.data?.title
            val category = bean.data?.category
            val photo = bean.data?.cover?.feed

            val desc = bean.data?.description
            val duration = bean.data?.duration
            val playUrl = bean.data?.playUrl
            val blurred = bean.data?.cover?.blurred
            val collect = bean.data?.consumption?.collectionCount
            val share = bean.data?.consumption?.shareCount
            val reply = bean.data?.consumption?.replyCount
            val time = System.currentTimeMillis()

            val videoBean = VideoBean(
                    photo,
                    title,
                    desc,
                    duration,
                    playUrl,
                    category,
                    blurred,
                    collect,
                    share,
                    reply,
                    time)

            val url = SPUtils.getInstance(context!!, "beans").getString(playUrl!!)
            if (url == "") {
                var count = SPUtils.getInstance(context!!, "beans").getInt("count")
                count = if (count != -1) {
                    count.inc()
                } else {
                    1
                }
                SPUtils.getInstance(context!!, "beans").put("count", count)
                SPUtils.getInstance(context!!, "beans").put(playUrl, playUrl)
                ObjectSaveUtils.saveObject(context!!, "bean$count", videoBean)
            }

            //跳转视频详情页
            val intent = Intent(context, VideoDetailActivity::class.java)
            intent.putExtra("dataFlag", videoBean as Parcelable)
            context!!.startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = baseQuickAdapter

        baseQuickAdapter.setOnLoadMoreListener({
            if (dataFlag != null) {
                mPresenter.moreData(dataFlag)
            }
        }, recyclerView)

        //下拉刷新监听
        refreshLayout.setOnRefreshListener {
            mPresenter.start()
        }
    }
}