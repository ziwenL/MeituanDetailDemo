package com.ziwenl.meituan_detail.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * PackageName : com.ziwenl.meituan_detail.widgets
 * Author : Ziwen Lan
 * Date : 2020/9/28
 * Time : 15:30
 * Introduction :
 */
class MyViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    var isScrollable = true


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isScrollable && super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isScrollable && super.onTouchEvent(ev)
    }
}