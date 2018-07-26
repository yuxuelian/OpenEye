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
import com.base.openeye.mvp.model.bean.VideoBean
import com.base.openeye.ui.VideoDetailActivity
import com.base.openeye.utils.ObjectSaveUtils
import com.base.openeye.utils.SPUtils
import com.base.openeye.utils.defaultLoadImage
import java.text.SimpleDateFormat

/**
 * Created by lvruheng on 2017/7/7.
 */
class WatchAdapter(context: Context, list: ArrayList<VideoBean>) : RecyclerView.Adapter<WatchAdapter.WatchViewHolder>() {
    var context: Context? = null
    var list: ArrayList<VideoBean>? = null
    var inflater: LayoutInflater? = null

    init {
        this.context = context
        this.list = list
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WatchViewHolder {
        return WatchViewHolder(inflater?.inflate(R.layout.item_feed_result, parent, false), context!!)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: WatchViewHolder?, position: Int) {
        var photoUrl: String? = list?.get(position)?.feed

        holder?.iv_photo?.defaultLoadImage(photoUrl)

        var title: String? = list?.get(position)?.title
        holder?.tv_title?.text = title
        var category = list?.get(position)?.category
        var duration = list?.get(position)?.duration
        var minute = duration?.div(60)
        var second = duration?.minus((minute?.times(60)) as Long)
        var releaseTime = list?.get(position)?.time
        var smf: SimpleDateFormat = SimpleDateFormat("MM-dd")
        var date = smf.format(releaseTime)
        var realMinute: String
        var realSecond: String
        realMinute = if (minute!! < 10) {
            "0" + minute
        } else {
            minute.toString()
        }
        realSecond = if (second!! < 10) {
            "0" + second
        } else {
            second.toString()
        }
        holder?.tv_time?.text = "$category / $realMinute'$realSecond'' / $date"
        holder?.itemView?.setOnClickListener {
            //跳转视频详情页
            var intent = Intent(context, VideoDetailActivity::class.java)
            var desc = list?.get(position)?.description
            var playUrl = list?.get(position)?.playUrl
            var blurred = list?.get(position)?.blurred
            var collect = list?.get(position)?.collect
            var share = list?.get(position)?.share
            var reply = list?.get(position)?.reply
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


    class WatchViewHolder(itemView: View?, context: Context) : RecyclerView.ViewHolder(itemView) {
        var iv_photo: ImageView? = itemView?.findViewById(R.id.iv_photo)
        var tv_title: TextView? = itemView?.findViewById(R.id.tv_title)
        var tv_time: TextView? = itemView?.findViewById(R.id.tv_detail)

        init {
            tv_title?.typeface = App.fontFZLanTingHeiSDb

        }
    }
}