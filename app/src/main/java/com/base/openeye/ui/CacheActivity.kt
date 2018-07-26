package com.base.openeye.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.*
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.base.openeye.R
import com.base.openeye.adapter.DownloadAdapter
import com.base.openeye.mvp.model.bean.VideoBean
import com.base.openeye.utils.ObjectSaveUtils
import com.base.openeye.utils.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_watch.*
import kotlinx.android.synthetic.main.app_title_bar.*
import zlc.season.rxdownload3.RxDownload
import zlc.season.rxdownload3.core.DownloadConfig.context
import java.lang.ref.WeakReference

/**
 * Created by lvruheng on 2017/7/12.
 */
class CacheActivity : BaseActivity() {

    private lateinit var mAdapter: BaseQuickAdapter<VideoBean, BaseViewHolder>

    override fun isLightStatusBar(): Boolean = true

    override fun getTitleView(): View? = appTitleLayout

    private val mHandler: Handler by lazy {
        MyHandler(this)
    }

    private class MyHandler(cacheActivity: CacheActivity) : Handler() {
        private val weakReference: WeakReference<CacheActivity> = WeakReference(cacheActivity)
        override fun handleMessage(msg: Message) {
            weakReference.get()?.let { cacheActivity ->
                val list = msg.data.getParcelableArrayList<VideoBean>("beans")
                list?.let { list1 ->
                    if (list1.size == 0) {
                        cacheActivity.tv_hint.visibility = View.VISIBLE
                    } else {
                        cacheActivity.tv_hint.visibility = View.GONE
                        cacheActivity.mAdapter.setNewData(list)
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)

        setToolbar()

        DataAsyncTask(mHandler, this).execute()

        recyclerView.layoutManager = LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            it
        }

        mAdapter = DownloadAdapter(R.layout.item_download)

        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as? VideoBean
            AlertDialog
                    .Builder(this)
                    .setMessage("是否删除当前视频")
                    .setNegativeButton("否", { dialog, which ->
                        dialog.dismiss()
                    })
                    .setPositiveButton("是", { dialog, which ->
                        RxDownload
                                .delete(item?.playUrl!!, true)
                                .subscribe()

                        SPUtils
                                .getInstance(this, "downloads")
                                .put(item.playUrl!!, "")

                        val count = position + 1

                        ObjectSaveUtils.deleteFile("download$count", this)

                        mAdapter.data.removeAt(position)
                        mAdapter.notifyItemRemoved(position)
                    })
                    .show()
            false
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter.data[position]

            //跳转视频详情页
            val intent = Intent(context, VideoDetailActivity::class.java)

            val time = System.currentTimeMillis()

            val videoBean = VideoBean(
                    item.feed,
                    item.title,
                    item.description,
                    item.duration,
                    item.playUrl,
                    item.category,
                    item.blurred,
                    item.collect,
                    item.share,
                    item.reply,
                    time)

            intent.putExtra("dataFlag", videoBean as Parcelable)

            RxDownload
                    .file(item.playUrl!!)
                    .subscribe {
                        val uri = Uri.fromFile(it)
                        intent.putExtra("loaclFile", uri.toString())
                    }

            context?.startActivity(intent)
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter.data[position]
            when (view.id) {
                R.id.iv_download_state -> {
                    val isDownload = SPUtils
                            .getInstance(this, "download_state")
                            .getBoolean(item.playUrl!!)
                    if (isDownload) {
                        SPUtils
                                .getInstance(context!!, "download_state")
                                .put(item.playUrl!!, false)

                        (view as? ImageView)?.setImageResource(R.drawable.icon_download_start)


                        RxDownload.stop(item.playUrl!!).subscribe()
                    } else {
                        SPUtils
                                .getInstance(context!!, "download_state")
                                .put(item.playUrl!!, true)

                        (view as? ImageView)?.setImageResource(R.drawable.icon_download_stop)

                        addMission(item.playUrl!!)
                    }
                }
            }
        }

        recyclerView.adapter = mAdapter
    }

    private fun addMission(playUrl: String) {
        RxDownload.start(playUrl).subscribe({
            Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(context, "添加任务失败", Toast.LENGTH_SHORT).show()
        })
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        val bar = supportActionBar
        bar?.title = getString(R.string.my_cache)
        bar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private class DataAsyncTask(handler: Handler, activity: CacheActivity) : AsyncTask<Void, Void, ArrayList<VideoBean>>() {
        @SuppressLint("StaticFieldLeak")
        var activity: CacheActivity = activity

        var handler = handler
        override fun doInBackground(vararg params: Void?): ArrayList<VideoBean>? {
            var list = ArrayList<VideoBean>()
            var count: Int = SPUtils.getInstance(activity, "downloads").getInt("count")
            var i = 1
            while (i.compareTo(count) <= 0) {
                var bean: VideoBean
                if (ObjectSaveUtils.getValue(activity, "download$i") == null) {
                    continue
                } else {
                    bean = ObjectSaveUtils.getValue(activity, "download$i") as VideoBean
                }
                list.add(bean)
                i++
            }
            return list
        }

        override fun onPostExecute(result: ArrayList<VideoBean>?) {
            super.onPostExecute(result)
            var message = handler.obtainMessage()
            var bundle = Bundle()
            bundle.putParcelableArrayList("beans", result)
            message.data = bundle
            handler.sendMessage(message)
        }
    }
}