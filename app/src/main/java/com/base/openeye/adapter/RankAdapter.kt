package com.base.openeye.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.base.openeye.App
import com.base.openeye.R
import com.base.openeye.mvp.model.bean.HotBean
import com.base.openeye.mvp.model.bean.VideoBean
import com.base.openeye.ui.VideoDetailActivity
import com.base.openeye.utils.ObjectSaveUtils
import com.base.openeye.utils.SPUtils
import com.base.openeye.utils.defaultLoadImage

/**
 * Created by lvruheng on 2017/7/7.
 */
class RankAdapter(context: Context, list: ArrayList<HotBean.ItemListBean.DataBean>) : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {
    var context: Context? = null;
    var list: ArrayList<HotBean.ItemListBean.DataBean>? = null
    var inflater: LayoutInflater? = null

    init {
        this.context = context
        this.list = list
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RankViewHolder {
        return RankViewHolder(inflater?.inflate(R.layout.item_rank, parent, false), context!!)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: RankViewHolder?, position: Int) {
        var photoUrl: String? = list?.get(position)?.cover?.feed

        holder?.iv_photo?.defaultLoadImage(photoUrl)

        var title: String? = list?.get(position)?.title
        holder?.tv_title?.text = title
        var category = list?.get(position)?.category
        var duration = list?.get(position)?.duration
        var minute = duration?.div(60)
        var second = duration?.minus((minute?.times(60)) as Long)
        var realMinute: String
        var realSecond: String
        if (minute!! < 10) {
            realMinute = "0" + minute
        } else {
            realMinute = minute.toString()
        }
        if (second!! < 10) {
            realSecond = "0" + second
        } else {
            realSecond = second.toString()
        }
        holder?.tv_time?.text = "$category / $realMinute'$realSecond''"
        holder?.itemView?.setOnClickListener {
            //跳转视频详情页
            var intent: Intent = Intent(context, VideoDetailActivity::class.java)
            var desc = list?.get(position)?.description
            var playUrl = list?.get(position)?.playUrl
            var blurred = list?.get(position)?.cover?.blurred
            var collect = list?.get(position)?.consumption?.collectionCount
            var share = list?.get(position)?.consumption?.shareCount
            var reply = list?.get(position)?.consumption?.replyCount
            var time = System.currentTimeMillis()
            var videoBean = VideoBean(photoUrl, title, desc, duration, playUrl, category, blurred, collect, share, reply, time)
            var url = SPUtils.getInstance(context!!, "beans").getString(playUrl!!)
            if (url.equals("")) {
                var count = SPUtils.getInstance(context!!, "beans").getInt("count")
                if (count != -1) {
                    count = count.inc()
                } else {
                    count = 1
                }
                SPUtils.getInstance(context!!, "beans").put("count", count)
                SPUtils.getInstance(context!!, "beans").put(playUrl!!, playUrl)
                ObjectSaveUtils.saveObject(context!!, "bean$count", videoBean)
            }
            intent.putExtra("dataFlag", videoBean as Parcelable)
            context?.let { context -> context.startActivity(intent) }
        }
    }


    class RankViewHolder(itemView: View?, context: Context) : RecyclerView.ViewHolder(itemView) {
        var iv_photo: ImageView? = itemView?.findViewById(R.id.iv_photo)
        var tv_title: TextView? = itemView?.findViewById(R.id.tv_title)
        var tv_time: TextView? = itemView?.findViewById(R.id.tv_time)

        init {
            tv_title?.typeface = App.fontFZLanTingHeiSDb

        }
    }
}