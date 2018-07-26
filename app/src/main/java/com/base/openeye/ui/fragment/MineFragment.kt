package com.base.openeye.ui.fragment

import android.content.Intent
import com.base.openeye.App
import com.base.openeye.R
import com.base.openeye.ui.AdviseActivity
import com.base.openeye.ui.CacheActivity
import com.base.openeye.ui.LottieTestActivity
import com.base.openeye.ui.WatchActivity
import kotlinx.android.synthetic.main.mine_fragment.*

/**
 * Created by lvruheng on 2017/7/4.
 */
class MineFragment : BaseFragment() {
    override fun getLayoutResources(): Int {
        return R.layout.mine_fragment
    }

    override fun initView() {
        tv_save.setOnClickListener {
            //startActivity(Intent(activity, CacheActivity::class.java))
            startActivity(Intent(activity, LottieTestActivity::class.java))
        }

        tv_watch.setOnClickListener {
            startActivity(Intent(activity, WatchActivity::class.java))
        }

        tv_advise.setOnClickListener {
            startActivity(Intent(activity, AdviseActivity::class.java))
        }

        tv_save.typeface = App.fontFZLanTingHeiSDb1
        tv_watch.typeface = App.fontFZLanTingHeiSDb1
        tv_advise.typeface = App.fontFZLanTingHeiSDb1
    }

}