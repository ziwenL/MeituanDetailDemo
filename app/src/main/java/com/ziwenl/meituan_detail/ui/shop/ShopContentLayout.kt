package com.ziwenl.meituan_detail.ui.shop

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.ui.shop.adapter.ViewPagerAdapter
import com.ziwenl.meituandemo.ui.store.EvaluateFragment
import com.ziwenl.meituandemo.ui.store.ShopFragment
import kotlinx.android.synthetic.main.shop_details_content.view.*

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 11:04
 * Introduction : 店铺详情 -- 中下部分主内容（点菜/评价/商家）
 */
class ShopContentLayout : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private lateinit var mFragmentList: MutableList<Fragment>
    private var mShopContentBehavior: ShopContentBehavior? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.shop_details_content, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //点菜、评价、商家 Fragment
        mFragmentList = mutableListOf()
        mFragmentList.add(MenuFragment.getInstance(object : MenuFragment.LayoutExpandControl {
            override fun fold() {
                mShopContentBehavior?.fold()
            }

            override fun isExpanded(): Boolean {
                return mShopContentBehavior?.isExpanded() ?: false
            }
        }))
        mFragmentList.add(EvaluateFragment.getInstance())
        mFragmentList.add(ShopFragment.getInstance())
        val vpAdapter =
            ViewPagerAdapter((context as AppCompatActivity).supportFragmentManager, mFragmentList)
        vp_main.adapter = vpAdapter
        vp_main.offscreenPageLimit = mFragmentList.size

        tab_layout.setViewPager(vp_main, arrayOf("点菜", "评价", "商家"))
    }

    fun getScrollableView(): View {
        val view =
            (mFragmentList[vp_main.currentItem] as ScrollableViewProvider).getScrollableView()
        return view
    }

    fun getRootScrollView(): View? {
        return (mFragmentList[vp_main.currentItem] as ScrollableViewProvider).getRootScrollView()
    }

    fun setShopContentBehavior(shopContentBehavior: ShopContentBehavior) {
        mShopContentBehavior = shopContentBehavior
    }
}

interface ScrollableViewProvider {
    fun getScrollableView(): View
    fun getRootScrollView(): View? {
        return null
    }
}