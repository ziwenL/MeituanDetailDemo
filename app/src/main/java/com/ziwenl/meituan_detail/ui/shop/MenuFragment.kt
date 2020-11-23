package com.ziwenl.meituan_detail.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.ui.shop.adapter.FloatDecoration
import com.ziwenl.meituan_detail.widgets.CenterLayoutManager
import com.ziwenl.meituandemo.bean.MenuChildBean
import com.ziwenl.meituandemo.bean.MenuTabBean
import com.ziwenl.meituandemo.ui.store.adapter.MenuLeftAdapter
import com.ziwenl.meituandemo.ui.store.adapter.MenuRightAdapter
import kotlinx.android.synthetic.main.fragment_shop_details_menu.*
import kotlinx.android.synthetic.main.item_shop_details_menu_right_group.view.*


/**
 * PackageName : com.ziwenl.meituandemo.ui.store
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 10:15
 * Introduction : 点菜（菜单）
 */
class MenuFragment : Fragment(), ScrollableViewProvider {
    private val mLeftData = mutableListOf<MenuTabBean>()
    private val mRightData = mutableListOf<MenuChildBean>()
    private lateinit var mLeftAdapter: MenuLeftAdapter
    private lateinit var mLayoutControl: LayoutExpandControl
    private var mIsClickFold = false
    private var mRvState = RecyclerView.State()
    private lateinit var mLeftLayoutManager:CenterLayoutManager

    companion object {
        fun getInstance(
            layoutExpandControl: LayoutExpandControl
        ): MenuFragment {
            val fragment = MenuFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.setLayoutExpandControl(layoutExpandControl)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_details_menu, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //初始化
        mLeftAdapter = MenuLeftAdapter(mLeftData)
        mLeftLayoutManager=CenterLayoutManager(context)
        rv_left.layoutManager = mLeftLayoutManager
        rv_left.adapter = mLeftAdapter
        val rightAdapter = MenuRightAdapter(mRightData)
        rv_right.layoutManager = LinearLayoutManager(context)
        rv_right.adapter = rightAdapter
        //左边 RecyclerView item 点击事件监听
        mLeftAdapter.setCallback(object : MenuLeftAdapter.Callback {
            override fun onClickItem(position: Int) {
                for (i in 0 until mRightData.size) {
                    if (mLeftData[position].name == mRightData[i].groupName) {
                        //未折叠时进行折叠
                        if (mLayoutControl.isExpanded()) {
                            mLayoutControl.fold()
                            mIsClickFold = true
                        }
                        if (rv_right.layoutParams.height != scrollView.height) {
                            rv_right.layoutParams.height = scrollView.height
                            rv_left.layoutParams.height = scrollView.height
                            rv_right.layoutParams = rv_right.layoutParams
                            rv_left.layoutParams = rv_left.layoutParams
                        }
                        //右边菜品 RecyclerView 将指定 item 滚动到可见第一条
                        (rv_right.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            i,
                            0
                        )
                        mLeftAdapter.currentPosition = position
                        mLeftAdapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        })

        //右边 RecyclerView 添加悬浮吸顶装饰
        rv_right.addItemDecoration(
            FloatDecoration(
                context!!,
                rv_right,
                R.layout.item_shop_details_menu_right_group,
                object : FloatDecoration.DecorationCallback {
                    override fun getDecorationFlag(position: Int): String {
                        //区分不同条目装饰 View 的 Flag
                        return mRightData[position].groupName
                    }

                    override fun onBindView(decorationView: View, position: Int) {
                        //装饰 View 数据绑定
                        decorationView.tv_group_name.text = mRightData[position].groupName
                    }
                })
        )

        //右边 RecyclerView 滚动监听
        rv_right.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //实现右边滚动联动左边 RecyclerView
                val position =
                    (rv_right.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (mLeftData[mLeftAdapter.currentPosition].name != mRightData[position].groupName) {
                    for (i in 0 until mLeftData.size) {
                        if (mLeftData[i].name == mRightData[position].groupName) {
                            mLeftAdapter.currentPosition = i
                            mLeftAdapter.notifyDataSetChanged()
                            mLeftLayoutManager.smoothScrollToPosition(
                                rv_left,
                                mRvState,
                                mLeftAdapter.currentPosition
                            )
                            break
                        }
                    }
                }
            }
        })
        //根据屏幕实际宽高设置两个recyclerView高度为固定值
        scrollView.post {
            rv_right.layoutParams.height = scrollView.height
            rv_left.layoutParams.height = scrollView.height
            rv_right.layoutParams = rv_right.layoutParams
            rv_left.layoutParams = rv_left.layoutParams
        }

        //记录是 rv_left 被触摸还是 rv_right 被触摸
        val onTouchListener = View.OnTouchListener { v, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN && v.id == R.id.rv_right -> {
                    mIsTouchRvRight = true
                }
                event.action == MotionEvent.ACTION_UP && v.id == R.id.rv_right -> {
                    mIsTouchRvRight = false
                }
                event.action == MotionEvent.ACTION_DOWN && v.id == R.id.rv_left -> {
                    mIsTouchRvLeft = true
                }
                event.action == MotionEvent.ACTION_UP && v.id == R.id.rv_left -> {
                    mIsTouchRvLeft = false
                }
            }
            false
        }
        rv_right.setOnTouchListener(onTouchListener)
        rv_left.setOnTouchListener(onTouchListener)
        //scrollView 滚动监听
        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val dy = scrollY - oldScrollY
            //上滑时，如果 bannerView 尚未被滚出屏幕，则不允许左右两个 RecyclerView 滚动 （通过 offsetChildrenVertical(dy) 实现两个 rv 未滚动的假象）
            if (dy > 0) {
                if (scrollY < fl_banner.height) {
                    if (mIsTouchRvRight) {
                        rv_right.offsetChildrenVertical(dy)
                    }
                    if (mIsTouchRvLeft) {
                        rv_left.offsetChildrenVertical(dy)
                    }
                }
            }
        })
        //添加假数据
        getData()
    }

    private var mIsTouchRvRight = false
    private var mIsTouchRvLeft = false

    override fun getScrollableView(): View {
        //如果左/右边 recyclerView 还能往上滚，则将左/右边的 recyclerView 暴露出去用来判断滑动
        if (rv_right.canScrollVertically(-1)) {
            return rv_right
        } else if (rv_left.canScrollVertically(-1)) {
            return rv_left
        } else {
            return scrollView
        }
    }

    override fun getRootScrollView(): View? {
        return scrollView
    }

    private fun getData() {
        //假数据
        mLeftData.add(MenuTabBean("收藏福利"))
        mLeftData.add(MenuTabBean("一人食"))
        mLeftData.add(MenuTabBean("新品尝鲜"))
        mLeftData.add(MenuTabBean("推荐"))
        mLeftData.add(MenuTabBean("折扣"))
        mLeftData.add(MenuTabBean("买一送一"))
        mLeftData.add(MenuTabBean("精选套餐"))
        mLeftData.add(MenuTabBean("企业团餐"))
        mLeftData.add(MenuTabBean("意面小吃"))
        mLeftData.add(MenuTabBean("下午时光"))
        mLeftData.add(MenuTabBean("卡券专用"))
        mLeftData.add(MenuTabBean("饮品"))
        mLeftData.add(MenuTabBean("收藏福利1"))
        mLeftData.add(MenuTabBean("一人食1"))
        mLeftData.add(MenuTabBean("新品尝鲜1"))
        mLeftData.add(MenuTabBean("推荐1"))
        mLeftData.add(MenuTabBean("折扣1"))
        mLeftData.add(MenuTabBean("买一送一1"))
        mLeftData.add(MenuTabBean("精选套餐1"))
        mLeftData.add(MenuTabBean("企业团餐1"))
        mLeftData.add(MenuTabBean("意面小吃1"))
        mLeftData.add(MenuTabBean("下午时光1"))
        mLeftData.add(MenuTabBean("卡券专用1"))
        mLeftData.add(MenuTabBean("饮品1"))
        for (i in 0 until mLeftData.size) {
            mRightData.add(MenuChildBean(mLeftData.get(i).name, ""))
            mRightData.add(MenuChildBean(mLeftData.get(i).name, ""))
            mRightData.add(MenuChildBean(mLeftData.get(i).name, ""))
        }
        rv_left.adapter?.notifyDataSetChanged()
        rv_right.adapter?.notifyDataSetChanged()
    }

    private fun setLayoutExpandControl(layoutExpandControl: LayoutExpandControl) {
        mLayoutControl = layoutExpandControl
    }

    interface LayoutExpandControl {
        /**
         * 折叠布局
         */
        fun fold()

        /**
         * 布局是否展开
         */
        fun isExpanded(): Boolean
    }
}