package com.base.openeye.adapter

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.base.openeye.App
import com.base.openeye.R
import com.base.openeye.mvp.model.bean.VideoBean
import com.base.openeye.utils.SPUtils
import com.base.openeye.utils.download
import com.base.openeye.utils.defaultLoadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import io.reactivex.disposables.Disposable
import zlc.season.rxdownload3.RxDownload
import zlc.season.rxdownload3.core.*


/**
 * Created by lvruheng on 2017/7/7.
 */
class DownloadAdapter(layoutRes: Int) : BaseQuickAdapter<VideoBean, BaseViewHolder>(layoutRes) {

    companion object {
        val typeface by lazy { App.fontFZLanTingHeiSDb }
    }

    var isDownload = false

    private lateinit var disposable: Disposable

    override fun convert(helper: BaseViewHolder, item: VideoBean) {
        val tv_title = helper.getView<TextView>(R.id.tv_title)
        val iv_photo = helper.getView<ImageView>(R.id.iv_photo)
        val tv_detail = helper.getView<TextView>(R.id.tv_detail)
        val iv_download_state = helper.getView<ImageView>(R.id.iv_download_state)

        //设置字体
        tv_title.typeface = DownloadAdapter.typeface

        val title: String? = item.title
        val photoUrl: String? = item.feed

        //加载图片
        iv_photo.defaultLoadImage(photoUrl)
        tv_title.text = title

        isDownload = SPUtils.getInstance(DownloadConfig.context!!, "download_state").getBoolean(item.playUrl!!)

        if (isDownload) {
            iv_download_state?.setImageResource(R.drawable.icon_download_stop)
        } else {
            iv_download_state?.setImageResource(R.drawable.icon_download_start)
        }

        RxDownload
                .download(item.playUrl!!)
                .subscribe({
                    tv_detail.text = when (it) {
                        is Normal -> "开始"
                        is Suspend -> "已暂停 / $${it.percent()}%"
                        is Waiting -> "等待中"
                        is Downloading -> {
                            if (iv_download_state.visibility != View.VISIBLE) {
                                iv_download_state.visibility = View.VISIBLE
                            }
                            "缓存中 / ${it.percent()}%"
                        }
                        is Failed -> {
                            Log.w("Error", it.throwable)
                            "缓存失败"
                        }
                        is Succeed -> {
                            if (!disposable.isDisposed) {
                                disposable.dispose()
                            }

                            iv_download_state.visibility = View.GONE

                            isDownload = false

                            SPUtils
                                    .getInstance(DownloadConfig.context!!, "download_state")
                                    .put(item.playUrl!!, false)

                            SPUtils
                                    .getInstance(DownloadConfig.context!!, "hasLoaded")
                                    .put(item.playUrl!!, true)

                            "已缓存"
                        }
                        else -> ""
                    }
                })

        helper.addOnClickListener(R.id.iv_download_state)
    }
}