package com.ziwenl.meituan_detail.ui.shop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * PackageName : com.ziwenl.meituandemo.ui.store.adapter
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 11:53
 * Introduction :
 */
class ViewPagerAdapter(
    fm: FragmentManager,
    private var fragmentList: MutableList<Fragment>
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}