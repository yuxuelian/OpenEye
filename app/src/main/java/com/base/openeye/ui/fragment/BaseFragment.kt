package com.base.openeye.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.openeye.utils.inflate


/**
 * Created by lvruheng on 2017/7/4.
 */
abstract class BaseFragment : Fragment() {

    private var rootView: View? = null

    private var isFirst: Boolean = false

    private var isFragmentVisible: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = container?.inflate(getLayoutResources())
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isFragmentVisible = true
        }

        if (rootView == null) {
            return
        }

        //可见，并且没有加载过
        if (!isFirst && isFragmentVisible) {
            onFragmentVisibleChange(true)
            return
        }

        //由可见——>不可见 已经加载过
        if (isFragmentVisible) {
            onFragmentVisibleChange(false)
            isFragmentVisible = false
        }
    }

    open protected fun onFragmentVisibleChange(b: Boolean) {

    }

    abstract fun getLayoutResources(): Int

    abstract fun initView()
}