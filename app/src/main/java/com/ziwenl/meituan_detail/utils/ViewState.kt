package com.ziwenl.meituan_detail.utils

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * PackageName : com.ziwenl.meituan_detail.utils
 * Author : Ziwen Lan
 * Date : 2020/9/27
 * Time : 13:46
 * Introduction : View 特定属性持有对象
 */
class ViewState {
    var marginTop: Int = 0
    var marginBottom: Int = 0
    var marginLeft: Int = 0
    var marginRight: Int = 0

    var width: Int = 0
    var height: Int = 0

    var translationX: Float = 0F
    var translationY: Float = 0F

    var scaleX: Float = 0F
    var scaleY: Float = 0F

    var rotation: Float = 0F
    var alpha: Float = 0F


    fun scaleX(scaleX: Float): ViewState {
        this.scaleX = scaleX
        return this
    }

    fun sxBy(value: Float): ViewState {
        this.scaleX *= value
        return this
    }

    fun scaleY(scaleY: Float): ViewState {
        this.scaleY = scaleY
        return this
    }

    fun syBy(value: Float): ViewState {
        this.scaleY *= value
        return this
    }

    fun alpha(alpha: Float): ViewState {
        this.alpha = alpha
        return this
    }

    fun width(width: Int): ViewState {
        this.width = width
        return this
    }


    fun height(height: Int): ViewState {
        this.height = height
        return this
    }

    fun rotation(rotation: Float): ViewState {
        this.rotation = rotation
        return this
    }

    fun ws(s: Float): ViewState {
        this.width = (width * s).toInt()
        return this
    }

    fun hs(s: Float): ViewState {
        this.height = (height * s).toInt()
        return this
    }

    fun translationX(translationX: Float): ViewState {
        this.translationX = translationX
        return this
    }

    fun translationY(translationY: Float): ViewState {
        this.translationY = translationY
        return this
    }

    fun marginLeft(marginLeft: Int): ViewState {
        this.marginLeft = marginLeft
        return this
    }

    fun marginRight(marginRight: Int): ViewState {
        this.marginRight = marginRight
        return this
    }

    fun marginTop(marginTop: Int): ViewState {
        this.marginTop = marginTop
        return this
    }

    fun marginBottom(marginBottom: Int): ViewState {
        this.marginBottom = marginBottom
        return this
    }

    fun copy(view: View): ViewState {
        this.width = view.width
        this.height = view.height
        this.translationX = view.translationX
        this.translationY = view.translationY
        this.scaleX = view.scaleX
        this.scaleY = view.scaleY
        this.rotation = view.rotation
        this.alpha = view.alpha

        (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
            this.marginTop = it.topMargin
            this.marginBottom = it.bottomMargin
            this.marginLeft = it.leftMargin
            this.marginRight = it.rightMargin
        }

        return this
    }
}

/**
 *  --------- 拓展方法 ---------
 */

/**
 * 通过 setTag 方式保存 view 状态
 */
fun View.stateSet(tag: Int, vs: ViewState) {
    setTag(tag, vs)
}

/**
 *  view 状态读取
 */
fun View.stateRead(tag: Int): ViewState? {
    return getTag(tag) as? ViewState
}

/**
 * 设置并保存 view 状态
 */
fun View.stateSave(tag: Int): ViewState {
    val vs = stateRead(tag) ?: ViewState()
    vs.copy(this)
    stateSet(tag, vs)
    return vs
}

/**
 * 根据指定 View 记录的两种状态来过渡更新 View 状态
 * 如果本身有设置 AnimationUpdateListener 监听则执行 onAnimationUpdate 回调覆盖操作
 * @param startTag 初始状态对应记录 Id
 * @param endTag 最终状态对应记录 Id
 * @param p 变化进度 [0,1]
 */
fun View.stateRefresh(startTag: Int, endTag: Int, p: Float) {
    if (this is AnimationUpdateListener) {
        onAnimationUpdate(startTag, endTag, p)
    } else {
        val startViewState = stateRead(startTag)
        val endViewState = stateRead(endTag)
        if (startViewState != null && endViewState != null) {
            if (startViewState.translationX != endViewState.translationX) translationX =
                startViewState.translationX + (endViewState.translationX - startViewState.translationX) * p
            if (startViewState.translationY != endViewState.translationY) translationY =
                startViewState.translationY + (endViewState.translationY - startViewState.translationY) * p
            if (startViewState.scaleX != endViewState.scaleX) scaleX =
                startViewState.scaleX + (endViewState.scaleX - startViewState.scaleX) * p
            if (startViewState.scaleY != endViewState.scaleY) scaleY =
                startViewState.scaleY + (endViewState.scaleY - startViewState.scaleY) * p
            if (startViewState.rotation != endViewState.rotation) rotation =
                (startViewState.rotation + (endViewState.rotation - startViewState.rotation) * p) % 360
            if (startViewState.alpha != endViewState.alpha) alpha =
                startViewState.alpha + (endViewState.alpha - startViewState.alpha) * p

            val o = layoutParams
            var lpChanged = false
            if (startViewState.width != endViewState.width) {
                o.width =
                    (startViewState.width + (endViewState.width - startViewState.width) * p).toInt()
                lpChanged = true
            }
            if (startViewState.height != endViewState.height) {
                o.height =
                    (startViewState.height + (endViewState.height - startViewState.height) * p).toInt()
                lpChanged = true
            }
            (o as? ViewGroup.MarginLayoutParams)?.let {
                if (startViewState.marginTop != endViewState.marginTop) {
                    it.topMargin =
                        (startViewState.marginTop + (endViewState.marginTop - startViewState.marginTop) * p).toInt()
                    lpChanged = true
                }
                if (startViewState.marginBottom != endViewState.marginBottom) {
                    it.bottomMargin =
                        (startViewState.marginBottom + (endViewState.marginBottom - startViewState.marginBottom) * p).toInt()
                    lpChanged = true
                }
                if (startViewState.marginLeft != endViewState.marginLeft) {
                    it.leftMargin =
                        (startViewState.marginLeft + (endViewState.marginLeft - startViewState.marginLeft) * p).toInt()
                    lpChanged = true
                }
                if (startViewState.marginRight != endViewState.marginRight) {
                    it.topMargin =
                        (startViewState.marginRight + (endViewState.marginRight - startViewState.marginRight) * p).toInt()
                    lpChanged = true
                }
            }
            if (lpChanged) layoutParams = o
        }
    }
}

/**
 * 通过属性动画更新指定 View 状态
 */
fun Any?.statesChangeByAnimation(
    views: Array<View>,
    startTag: Int,
    endTag: Int,
    start: Float = 0F,
    end: Float = 1F,
    updateCallback: AnimationUpdateListener? = null,
    updateStateListener: AnimatorListenerAdapter? = null,
    duration: Long = 400L,
    startDelay: Long = 0L
): ValueAnimator {
    return ValueAnimator.ofFloat(start, end).apply {
        this.startDelay = startDelay
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation ->
            val p = animation.animatedValue as Float
            updateCallback?.onAnimationUpdate(startTag, endTag, p)
            for (it in views) it.stateRefresh(startTag, endTag, animation.animatedValue as Float)
        }
        updateStateListener?.let { addListener(it) }
        start()
    }
}

/**
 * 动画更新监听器
 */
interface AnimationUpdateListener {
    fun onAnimationUpdate(tag1: Int, tag2: Int, p: Float)
}



