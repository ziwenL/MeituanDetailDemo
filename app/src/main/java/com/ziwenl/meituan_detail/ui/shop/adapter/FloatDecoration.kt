package com.ziwenl.meituan_detail.ui.shop.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView


/**
 * PackageName : com.ziwenl.meituandemo.ui.store.adapter
 * Author : Ziwen Lan
 * Date : 2020/9/15
 * Time : 13:51
 * Introduction : RecyclerView 悬浮吸顶 Decoration
 * 填充的 decorationLayoutRes 根布局高度需为明确值
 */
class FloatDecoration(
    context: Context,
    /**
     * 被装饰的 RecyclerView
     */
    recyclerView: RecyclerView,
    /**
     * 布局 layout
     */
    @LayoutRes decorationLayoutRes: Int,
    /**
     * 回调接口
     */
    decorationCallback: DecorationCallback
) : RecyclerView.ItemDecoration() {

    private var mDecorationView: View =
        LayoutInflater.from(context).inflate(decorationLayoutRes, recyclerView, false)
    private var mDecorationHeight: Int = 0
    private var mCallback: DecorationCallback

    init {
        //计算装饰 View 的总高度
        if (mDecorationView.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mDecorationHeight = 0
        } else {
            mDecorationHeight = mDecorationView.layoutParams.height
        }
        mCallback = decorationCallback
    }

    /**
     * 设置 Item 偏移量
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (isShowDecoration(position)) {
            outRect.top = mDecorationHeight
        } else {
            outRect.top = 0
        }
    }

    /**
     * 在 Canvas 上绘制内容，于绘制 item 之后，会覆盖 item
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (mDecorationHeight == 0) return
        val itemCount = state.itemCount
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        var preDecoration = ""
        var currentDecoration = ""
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            preDecoration = currentDecoration
            currentDecoration = mCallback.getDecorationFlag(position)
            if (preDecoration == currentDecoration) {
                continue
            }
            val viewBottom = view.bottom
            var top = Math.max(mDecorationHeight, view.top).toFloat()
            //下一个和当前不一样移动当前
            if (position + 1 < itemCount) {
                val nextDecoration = mCallback.getDecorationFlag(position + 1)
                //组内最后一个view进入了header
                if (nextDecoration !== currentDecoration && viewBottom < top) {
                    top = viewBottom.toFloat()
                }
            }
            mCallback.onBindView(mDecorationView, position)
            mDecorationView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            mDecorationView.layout(left, 0, right, mDecorationHeight)
            mDecorationView.isDrawingCacheEnabled = true
            c.drawBitmap(
                mDecorationView.drawingCache,
                left.toFloat(),
                top - mDecorationHeight,
                null
            )
            mDecorationView.isDrawingCacheEnabled = false
            mDecorationView.destroyDrawingCache()
        }
    }


    private fun isShowDecoration(position: Int): Boolean {
        if (position == 0) {
            return true
        } else {
            val preDecoration = mCallback.getDecorationFlag(position - 1)
            val currentDecoration = mCallback.getDecorationFlag(position)
            return preDecoration != currentDecoration
        }
    }

    /**
     * 回调接口
     */
    interface DecorationCallback {
        /**
         * 获得装饰标识位：用来区分不同分组，可以根据实际需求调整为 String(组名) 或者是 Long(组Id) 类型
         */
        fun getDecorationFlag(position: Int): String

        /**
         * 操作装饰View：填充数据
         */
        fun onBindView(decorationView: View, position: Int)
    }
}