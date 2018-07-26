package com.base.openeye.ui

import android.os.Bundle
import android.view.View
import com.base.openeye.R
import kotlinx.android.synthetic.main.activity_advise.*
import kotlinx.android.synthetic.main.app_title_bar.*

/**
 * Created by lvruheng on 2017/7/11.
 */
class AdviseActivity : BaseActivity() {
    override fun isLightStatusBar(): Boolean = true

    override fun getTitleView(): View? = appTitleLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advise)
        settoolbar()
    }

    fun settoolbar() {
        setSupportActionBar(toolbar)
        var bar = supportActionBar
        bar?.title = "意见反馈"
        bar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}