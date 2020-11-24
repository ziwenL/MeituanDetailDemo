package com.ziwenl.meituan_detail.ui.shop

import android.animation.Animator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.utils.dp
import com.ziwenl.meituan_detail.widgets.MyViewPager
import kotlin.math.abs

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 11:54
 * Introduction :
 */
class ShopContentBehavior(private val context: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<ShopContentLayout>(context, attrs) {

    /**
     * 顶部标题栏：返回、搜索、收藏、更多
     */
    private lateinit var mShopTitleLayoutView: ShopTitleLayout

    /**
     * 中上部分店铺信息：配送时间、描述、评分、优惠及公告
     */
    private lateinit var mShopDiscountLayoutView: ShopDiscountLayout

    /**
     * 中下部分：点菜（广告、菜单）、评价、商家
     */
    private lateinit var mShopContentLayoutView: ShopContentLayout

    /**
     * 底部价格：满减神器、满减优惠、选中价格
     */
    private lateinit var mShopPriceLayoutView: ShopPriceLayout
    private val mScroller = Scroller(context)
    private val mScrollDuration = 800
    private var mSimpleTopDistance = 0
    private var mIsScrollToFullFood = false // 上滑显示商品菜单
    private var mIsScrollToHideFood = false // 下滑显示商店详情
    private var mVerticalPagingTouch = 0 // 菜单竖项列表(商品，评价，商家)内容的触摸滑动距离
    private var mHorizontalPagingTouch = 0 // 菜单横项列表(推荐商品)内容的触摸滑动距离
    private var mIsScrollRecommends = false
    private lateinit var mVpMain: MyViewPager // 商品菜单所在pager
    private val mPagingTouchSlop = dp(5)

    //折叠偏移量
    private var mFoldingDy = 0
    private val mHandler = Handler()

    /**
     * 惯性滑动动画任务
     */
    private val mFlingRunnable = object : Runnable {
        override fun run() {
            if (mScroller.computeScrollOffset()) {
                mShopContentLayoutView.translationY = mScroller.currY.toFloat()
                mShopDiscountLayoutView.effectByOffset(mShopContentLayoutView.translationY)
                mShopPriceLayoutView.effectByOffset(mShopContentLayoutView.translationY)
                mHandler.post(this)
            } else {
                mIsScrollToHideFood = false
            }
        }
    }

    /**
     * 折叠任务
     */
    private val mFoldingRunnable = object : Runnable {
        override fun run() {
            if (mFoldingDy < mSimpleTopDistance) {
                mFoldingDy += 4//影响折叠速度
                layoutFolding(mFoldingDy)
                mHandler.post(this)
            } else {
                //折叠结束
            }
        }
    }

    /**
     * 滚动动画
     */
    private val mAnimListener = object : ShopDiscountLayout.AnimatorListenerAdapter1 {
        override fun onAnimationStart(animation: Animator?, toExpanded: Boolean) {
            //防止惯性滑动影响展开/闭合动画，所以要设置为true
            mIsFlingAndDown = true
            if (toExpanded) {
                val defaultDisplayHeight = (mShopContentLayoutView.height - mSimpleTopDistance)
                mScroller.startScroll(
                    0,
                    mShopContentLayoutView.translationY.toInt(),
                    0,
                    (defaultDisplayHeight - mShopContentLayoutView.translationY).toInt(),
                    mScrollDuration
                )
            } else {
                mScroller.startScroll(
                    0,
                    mShopContentLayoutView.translationY.toInt(),
                    0,
                    (-mShopContentLayoutView.translationY).toInt(),
                    mScrollDuration
                )
            }
            mHandler.post(mFlingRunnable)
            mIsScrollToHideFood = true
        }
    }

    /**
     * 确定使用Behavior的View位置
     */
    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: ShopContentLayout,
        layoutDirection: Int
    ): Boolean {
        if (!this::mShopContentLayoutView.isInitialized) {
            mShopContentLayoutView = child
            mShopContentLayoutView.setShopContentBehavior(this)
            mVpMain = child.findViewById(R.id.vp_main)
            mVpMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    mShopPriceLayoutView.visibility = if (position == 0) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            })

            val priceLayoutParams =
                mShopPriceLayoutView.layoutParams as CoordinatorLayout.LayoutParams
            priceLayoutParams.topMargin = parent.height - mShopPriceLayoutView.height
            mShopPriceLayoutView.layoutParams = priceLayoutParams

            val lp = mShopContentLayoutView.layoutParams as CoordinatorLayout.LayoutParams
            if (lp.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
                mSimpleTopDistance = lp.topMargin - mShopTitleLayoutView.height
                lp.height = parent.height - mShopTitleLayoutView.height
                child.layoutParams = lp
                return true
            }
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    /**
     * 确定使用 Behavior 的 View 要依赖的 View 的类型
     */
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ShopContentLayout,
        dependency: View
    ): Boolean {
        when (dependency.id) {
            R.id.layout_title -> mShopTitleLayoutView = dependency as ShopTitleLayout
            R.id.layout_discount -> mShopDiscountLayoutView =
                (dependency as ShopDiscountLayout).apply { animListener = mAnimListener }
            R.id.layout_price -> mShopPriceLayoutView = dependency as ShopPriceLayout
            else -> return false
        }
        return true
    }

    /**
     * 当被依赖的 View 状态改变时回调
     */
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ShopContentLayout,
        dependency: View
    ): Boolean = true

    /**
     * 是否处于惯性滑动且有触摸动作插入
     */
    private var mIsFlingAndDown = false

    /**
     * 嵌套滑动开始（ACTION_DOWN），确定 Behavior 是否要监听此次事件
     */
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ShopContentLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        if (mIsFling && type == ViewCompat.TYPE_TOUCH) {
            //处于惯性滑动时有触摸动作插入
            mIsFlingAndDown = true
        }
        return true
    }


    /**
     * 嵌套滑动进行中，要监听的子 View 将要滑动，滑动事件即将被消费（但最终被谁消费，可以通过代码控制）
     * @param type = ViewCompat.TYPE_TOUCH 表示是触摸引起的滚动 = ViewCompat.TYPE_NON_TOUCH 表示是触摸后的惯性引起的滚动
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ShopContentLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (mIsScrollToHideFood) {
            consumed[1] = dy
            return // scroller 滑动中.. do nothing
        }
        mVerticalPagingTouch += dy
        if (mVpMain.isScrollable && abs(mVerticalPagingTouch) > mPagingTouchSlop) {
            mVpMain.isScrollable = false // 屏蔽 pager横向滑动干扰
        }

        if (type == ViewCompat.TYPE_NON_TOUCH && mIsFlingAndDown) {
            //当处于惯性滑动时，有触摸动作进入，屏蔽惯性滑动，以防止滚动错乱
            consumed[1] = dy
            return
        }
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            mIsScrollToFullFood = true
        }
        mHorizontalPagingTouch += dx
        if ((child.translationY < 0 || (child.translationY == 0F && dy > 0))
            && !child.getScrollableView().canScrollVertically(-1)
        ) {
            val effect = mShopTitleLayoutView.effectByOffset(dy)
            val transY = -mSimpleTopDistance * effect
            mShopDiscountLayoutView.translationY = transY
            if (transY != child.translationY) {
                child.translationY = transY
                consumed[1] = dy
            }

        } else if ((child.translationY > 0 || (child.translationY == 0F && dy < 0))
            && !child.getScrollableView().canScrollVertically(-1)
        ) {
            if (mIsScrollToFullFood) {
                child.translationY = 0F
            } else {
                child.translationY -= dy
                mShopDiscountLayoutView.effectByOffset(child.translationY)
                mShopPriceLayoutView.effectByOffset(child.translationY)
            }
            consumed[1] = dy
        } else {
            //折叠状态
            if (child.getRootScrollView() != null
                //这个判断是防止按着bannerView滚动时导致scrollView滚动速度翻倍
                && (child.getScrollableView() is RecyclerView)
            ) {
                if (dy > 0) {
                    child.getRootScrollView()!!.scrollY += dy
                }
            }
        }
    }

    private var mIsFling = false

    /**
     * 接受嵌套滚动
     */
    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: ShopContentLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        mScroller.abortAnimation()
        mIsScrollToHideFood = false
        super.onNestedScrollAccepted(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    /**
     * 要监听的子View即将惯性滑动(开始非实际触摸的惯性滑动) type一定等于1
     */
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: ShopContentLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        //惯性滑动准备开始
        mIsFling = true
        mIsFlingAndDown = false
        return onUserStopDragging()
    }

    /**
     * 嵌套滑动结束（ ACTION_UP 或 ACTION_CANCEL ）
     */
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ShopContentLayout,
        target: View,
        type: Int
    ) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            //惯性滑动结束
            mIsFling = false
        }
        mIsScrollToFullFood = false
        mVerticalPagingTouch = 0
        mHorizontalPagingTouch = 0
        mIsScrollRecommends = false
        mVpMain.isScrollable = true
        if (!mIsScrollToHideFood) {
            onUserStopDragging()
        }
    }


    private fun onUserStopDragging(): Boolean {
        if (mShopContentLayoutView.translationY <= 0f) {
            return false
        }
        val defaultDisplayHeight = (mShopContentLayoutView.height - mSimpleTopDistance)
        if (defaultDisplayHeight * 0.4F > mShopContentLayoutView.translationY) {
            mScroller.startScroll(
                0,
                mShopContentLayoutView.translationY.toInt(),
                0,
                (-mShopContentLayoutView.translationY).toInt(),
                mScrollDuration
            )
            mShopDiscountLayoutView.switch(false, true)
        } else {
            mScroller.startScroll(
                0,
                mShopContentLayoutView.translationY.toInt(),
                0,
                (defaultDisplayHeight - mShopContentLayoutView.translationY).toInt(),
                mScrollDuration
            )
            mShopDiscountLayoutView.switch(true, true)
        }
        mHandler.post(mFlingRunnable)
        mIsScrollToHideFood = true
        return true
    }

    /**
     * 折叠
     */
    fun fold() {
        //移除惯性动画，防止重复设置 selfView.translationY
        mScroller.abortAnimation()
        mHandler.removeCallbacks(mFlingRunnable)
        //开始折叠
        mFoldingDy = 0
        mHandler.post(mFoldingRunnable)
    }

    /**
     * 是否展开
     * true 展开
     * false 折叠
     */
    fun isExpanded(): Boolean {
        return (-mShopContentLayoutView.translationY) < mSimpleTopDistance
    }

    /**
     * 通过动画使布局折叠
     */
    private fun layoutFolding(dy: Int) {
        mVerticalPagingTouch += dy
        val effect = mShopTitleLayoutView.effectByOffset(dy)
        val transY = -mSimpleTopDistance * effect
        mShopDiscountLayoutView.translationY = transY
        mShopContentLayoutView.translationY = transY
    }
}