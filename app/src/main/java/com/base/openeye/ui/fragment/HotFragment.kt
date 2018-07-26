package com.base.openeye.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.base.openeye.R
import kotlinx.android.synthetic.main.hot_fragment.*

/**
 * Created by lvruheng on 2017/7/4.
 */
class HotFragment : BaseFragment() {

    companion object {
        private val mTabs = listOf("周排行", "月排行", "总排行").toList()
        private val STRATEGY = arrayOf("weekly", "monthly", "historical").toList()
    }

    private val mFragments = ArrayList<Fragment>().let { arrayList ->
        STRATEGY.mapTo(arrayList) { strategy ->
            RankFragment().let { rankFragment ->
                rankFragment.arguments = Bundle().let { bundle ->
                    bundle.putString("strategy", strategy)
                    bundle
                }
                rankFragment
            }
        }
        arrayList
    }.toList()

    override fun getLayoutResources(): Int {
        return R.layout.hot_fragment
    }

    override fun initView() {
        vp_content.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mTabs.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return mTabs[position]
            }
        }

        tabs.setupWithViewPager(vp_content)
    }

}