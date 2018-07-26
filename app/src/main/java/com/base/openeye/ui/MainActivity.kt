package com.base.openeye.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.base.openeye.App
import com.base.openeye.R
import com.base.openeye.search.SearchFragment
import com.base.openeye.ui.fragment.FindFragment
import com.base.openeye.ui.fragment.HomeFragment
import com.base.openeye.ui.fragment.HotFragment
import com.base.openeye.ui.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_home_title_layout.*
import java.util.*


class MainActivity : BaseActivity() {

    private lateinit var searchFragment: SearchFragment

    val fragments = arrayListOf<Fragment>(
            HomeFragment(),
            FindFragment(),
            HotFragment(),
            MineFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewPager()

        //todo 点击搜索
//        searchFragment = SearchFragment()
//        searchFragment.show(supportFragmentManager, SEARCH_TAG)
    }

    override fun isLightStatusBar(): Boolean = true

    override fun getTitleView(): View? = appTitleLayout

    private fun getToday(): String {
        val list = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val data = Date()
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }

    private fun initViewPager() {
        titleText.typeface = App.fontLobster
        titleText.text = getToday()

        bottomNavigationViewEx.enableAnimation(false)
        bottomNavigationViewEx.enableShiftingMode(false)
        bottomNavigationViewEx.enableItemShiftingMode(false)

        homeViewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getCount(): Int = fragments.size
        }

        homeViewPager.offscreenPageLimit = 4

        bottomNavigationViewEx.setupWithViewPager(homeViewPager)
    }
}
