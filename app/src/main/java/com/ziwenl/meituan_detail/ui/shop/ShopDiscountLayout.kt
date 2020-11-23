package com.ziwenl.meituan_detail.ui.shop

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.utils.*
import kotlinx.android.synthetic.main.shop_details_discount.view.*
import kotlinx.android.synthetic.main.widget_shop_details_discount.view.*
import kotlinx.android.synthetic.main.widget_shop_details_discount_expanded.view.*

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 10:57
 * Introduction : 店铺详情 -- 顶部折扣活动布局
 */
class ShopDiscountLayout : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mIsExpanded = false
    private fun animViews(): Array<View> =
        arrayOf(tv_shop_name_b, cl_discount, cl_discount_expanded, view_top_bg_shadow)

    private var effected: Float = 0f
    private var firstLayout: Boolean = false

    private val internalAnimListener: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            animListener?.onAnimationStart(animation, mIsExpanded)
        }
    }

    var animListener: AnimatorListenerAdapter1? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.shop_details_discount, this)
        sv_main.setOnTouchListener { _, _ -> !mIsExpanded }

        tv_announcement.setOnClickListener { switch(!mIsExpanded) }
        iv_close.setOnClickListener { switch(!mIsExpanded) }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            //记录 View 状态
            view_top_bg_shadow.stateSave(R.id.viewStateStart).alpha(0F)
            view_top_bg_shadow.stateSave(R.id.viewStateEnd).alpha(1F)
            tv_shop_name_b.stateSave(R.id.viewStateStart).alpha(0.8F)
            tv_shop_name_b.stateSave(R.id.viewStateEnd).alpha(1F)
            cl_discount.stateSave(R.id.viewStateStart).alpha(1F)
            cl_discount.stateSave(R.id.viewStateEnd).alpha(0F)
            cl_discount_expanded.stateSave(R.id.viewStateStart).alpha(0F)
            cl_discount_expanded.stateSave(R.id.viewStateEnd).alpha(1F)
        }
    }

    /**
     * 根据业务需求动态计算变化高度区别
     */
    fun effectByOffset(transY: Float) {
        effected = when {
            transY <= dp(140) -> 0F
            transY > dp(140) && transY < dp(230) -> (transY - dp(140)) / dp(90)
            else -> 1F
        }
        animViews().forEach { it.stateRefresh(R.id.viewStateStart, R.id.viewStateEnd, effected) }
    }


    fun switch(
        expanded: Boolean,
        byScrollerSlide: Boolean = false
    ) {
        if (mIsExpanded == expanded) {
            return
        }
        sv_main.scrollTo(0, 0)
        mIsExpanded = expanded // 目标
        val start = effected
        val end = if (expanded) 1F else 0F
        statesChangeByAnimation(
            animViews(), R.id.viewStateStart, R.id.viewStateEnd, start, end,
            null, if (!byScrollerSlide) internalAnimListener else null, 500
        )
    }

    interface AnimatorListenerAdapter1 {
        fun onAnimationStart(animation: Animator?, toExpanded: Boolean)
    }
}