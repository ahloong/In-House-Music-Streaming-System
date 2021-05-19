package com.loong.ihms.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class CustomFragmentPagerAdapter : FragmentPagerAdapter {
    private var fragmentList: ArrayList<Fragment>
    private var fragmentTitleList = ArrayList<String>()

    constructor(
        fm: FragmentManager,
        fragmentList: ArrayList<Fragment>
    ) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.fragmentList = fragmentList
    }

    constructor(
        fm: FragmentManager,
        fragmentList: ArrayList<Fragment>,
        fragmentTitleList: ArrayList<String>
    ) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.fragmentList = fragmentList
        this.fragmentTitleList = fragmentTitleList
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun replaceList(fragmentList: ArrayList<Fragment>) {
        this.fragmentList = fragmentList
        notifyDataSetChanged()
    }
}