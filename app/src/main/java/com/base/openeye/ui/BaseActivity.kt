package com.base.openeye.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.base.openeye.utils.immersive

/**
 * @author xiaoka
 * @date 2018/1/6 上午8:40
 * GitHub：
 * email：
 * description：
 */

abstract class BaseActivity : AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        this.immersive(getTitleView(), isLightStatusBar())
    }

    abstract fun getTitleView(): View?
    abstract fun isLightStatusBar(): Boolean

}