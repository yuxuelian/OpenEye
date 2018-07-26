package com.base.openeye.ui

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.base.openeye.R
import com.base.openeye.adapter.WatchAdapter
import com.base.openeye.mvp.model.bean.VideoBean
import com.base.openeye.utils.ObjectSaveUtils
import com.base.openeye.utils.SPUtils
import kotlinx.android.synthetic.main.activity_watch.*
import kotlinx.android.synthetic.main.app_title_bar.*

/**
 * Created by lvruheng on 2017/7/11.
 */
class WatchActivity : BaseActivity() {
    var mList = ArrayList<VideoBean>()
    lateinit var mAdapter: WatchAdapter
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            var list = msg?.data?.getParcelableArrayList<VideoBean>("beans")
            if(list?.size?.compareTo(0) == 0){
                tv_hint.visibility = View.VISIBLE
            }else{
                tv_hint.visibility = View.GONE
                if(mList.size>0){
                    mList.clear()
                }
                list?.let { mList.addAll(it) }
                mAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun isLightStatusBar(): Boolean = true

    override fun getTitleView(): View? = appTitleLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)
        setToolbar()
        DataAsyncTask(mHandler,this).execute()
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = WatchAdapter(this, mList)
        recyclerView.adapter = mAdapter
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        var bar = supportActionBar
        bar?.title = "观看记录"
        bar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private class DataAsyncTask(handler: Handler, activity: WatchActivity) : AsyncTask<Void, Void, ArrayList<VideoBean>>() {
        var activity: WatchActivity = activity
        var handler = handler
        override fun doInBackground(vararg params: Void?): ArrayList<VideoBean>? {
            var list = ArrayList<VideoBean>()
            var count: Int = SPUtils.getInstance(activity, "beans").getInt("count")
            var i = 1
            while (i.compareTo(count) <= 0) {
                var bean :VideoBean= ObjectSaveUtils.getValue(activity, "bean$i") as VideoBean
                list.add(bean)
                i++
            }
            return list
        }

        override fun onPostExecute(result: ArrayList<VideoBean>?) {
            super.onPostExecute(result)
            var message = handler.obtainMessage()
            var bundle = Bundle()
            bundle.putParcelableArrayList("beans",result)
            message.data = bundle
            handler.sendMessage(message)
        }

    }
}