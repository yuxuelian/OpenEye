package com.base.openeye.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.base.openeye.App
import com.base.openeye.R
import com.base.openeye.mvp.model.bean.VideoBean
import com.base.openeye.utils.*
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.android.synthetic.main.activity_video_detail.*
import zlc.season.rxdownload3.RxDownload
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutionException


/**
 * Created by lvruheng on 2017/7/7.
 */
class VideoDetailActivity : BaseActivity() {

    companion object {
        var MSG_IMAGE_LOADED = 101
    }

    var mContext: Context = this

    var isPlay: Boolean = false

    var isPause: Boolean = false

    private val imageView: ImageView by lazy {
        ImageView(this).let {
            it.scaleType = ImageView.ScaleType.CENTER_CROP
            it
        }
    }

    private val orientationUtils by lazy {
        OrientationUtils(this, gsyPlayer)
    }

    private val videoBean: VideoBean by lazy {
        this.intent.getParcelableExtra<VideoBean>("dataFlag")
    }

    private val mHandler: Handler by lazy {
        MyHandler(this)
    }

    private class MyHandler(activity: VideoDetailActivity) : Handler() {
        private val weakReference: WeakReference<VideoDetailActivity> = WeakReference(activity)
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_IMAGE_LOADED -> {
                    weakReference.get()?.let {
                        it.gsyPlayer.thumbImageView = it.imageView
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        initView()
        prepareVideo()
    }

    override fun isLightStatusBar(): Boolean = true

    override fun getTitleView(): View? = null

    private fun initView() {
        var bgUrl = videoBean.blurred
        iv_bottom_bg?.displayHigh(bgUrl)

        tv_video_desc.text = videoBean.description
        tv_video_desc.typeface = App.fontFZLanTingHeiSDb
        tv_video_title.text = videoBean.title
        tv_video_title.typeface = App.fontFZLanTingHeiSDb
        var category = videoBean.category
        var duration = videoBean.duration
        var minute = duration?.div(60)
        var second = duration?.minus((minute?.times(60)) as Long)
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

        tv_video_time.text = "$category / $realMinute'$realSecond''"
        tv_video_favor.text = videoBean.collect.toString()
        tv_video_share.text = videoBean.share.toString()
        tv_video_reply.text = videoBean.share.toString()
        tv_video_download.setOnClickListener {
            //点击下载
            val url = videoBean.playUrl?.let {
                SPUtils.getInstance(this, "downloads").getString(it)
            }

            if (url.equals("")) {
                RxDownload
                        .download(videoBean.playUrl!!)
                        .subscribe({
                            showToast("开始下载")
                            SPUtils.getInstance(this, "downloads").put(videoBean.playUrl.toString(), videoBean.playUrl.toString())
                            SPUtils.getInstance(this, "download_state").put(videoBean.playUrl.toString(), true)
                        }, {
                            showToast("添加任务失败")
                        })
            } else {
                showToast("该视频已经缓存过了")
            }
        }
    }

    private fun prepareVideo() {
        var uri = intent.getStringExtra("loaclFile")
        if (uri != null) {
            Log.e("uri", uri)
            gsyPlayer.setUp(uri, false, null, null)
        } else {
            gsyPlayer.setUp(videoBean.playUrl, false, null, null)
        }

        //增加封面
        ImageViewAsyncTask(mHandler, this, imageView).execute(videoBean.feed)
        gsyPlayer.titleTextView.visibility = View.GONE
        gsyPlayer.backButton.visibility = View.VISIBLE

        gsyPlayer.setIsTouchWiget(true)

        //关闭自动旋转
        gsyPlayer.isRotateViewAuto = false

        gsyPlayer.isLockLand = false

        gsyPlayer.isShowFullAnimation = true

        gsyPlayer.isNeedLockFull = true

        gsyPlayer.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            gsyPlayer.startWindowFullscreen(mContext, true, true)
        }

        gsyPlayer.setStandardVideoAllCallBack(object : VideoListener() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                //开始播放了才能旋转和全屏
                orientationUtils.isEnable = true
                isPlay = true
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
                orientationUtils.backToProtVideo()
            }
        })

        gsyPlayer.setLockClickListener { view, lock ->
            //配合下方的onConfigurationChanged
            orientationUtils.isEnable = !lock
        }

        gsyPlayer.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private class ImageViewAsyncTask(handler: Handler, activity: VideoDetailActivity, private val mImageView: ImageView) : AsyncTask<String, Void, String>() {
        private var handler = handler
        private var mPath: String? = null
        private var mIs: FileInputStream? = null
        private var mActivity: VideoDetailActivity = activity
        override fun doInBackground(vararg params: String): String? {
            val future = Glide.with(mActivity)
                    .load(params[0])
                    .downloadOnly(100, 100)
            try {
                val cacheFile = future.get()
                mPath = cacheFile.absolutePath
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

            return mPath
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            try {
                mIs = FileInputStream(s)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val bitmap = BitmapFactory.decodeStream(mIs)
            mImageView.setImageBitmap(bitmap)
            var message = handler.obtainMessage()
            message.what = MSG_IMAGE_LOADED
            handler.sendMessage(message)
        }
    }

    override fun onBackPressed() {
        orientationUtils.let {
            orientationUtils.backToProtVideo()
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.let {
            orientationUtils.releaseListener()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            if (newConfig?.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!gsyPlayer.isIfCurrentIsFullscreen) {
                    gsyPlayer.startWindowFullscreen(mContext, true, true)
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (gsyPlayer.isIfCurrentIsFullscreen) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                orientationUtils?.let { orientationUtils.isEnable = true }
            }
        }
    }
}
