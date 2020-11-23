package com.ziwenl.meituan_detail.ui.shop

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.utils.AnimationUpdateListener
import com.ziwenl.meituan_detail.utils.stateRefresh
import com.ziwenl.meituan_detail.utils.stateSave
import kotlinx.android.synthetic.main.ticket_view.view.*

/**
 * PackageName : com.ziwenl.meituan_detail.ui.shop
 * Author : Ziwen Lan
 * Date : 2020/9/25
 * Time : 16:05
 * Introduction :
 */
class TicketView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs),
    AnimationUpdateListener {

    private var firstLayout: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ticket_view, this)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            vBorder1.stateSave(R.id.viewStateStart).alpha(1F)
            vBorder1.stateSave(R.id.viewStateEnd).ws(3.8F).hs(3.8F).alpha(0F)
            vBorder2.stateSave(R.id.viewStateStart).alpha(0F)
            vBorder2.stateSave(R.id.viewStateEnd).ws(3.8F).hs(3.8F).alpha(1F)
            vSimple.stateSave(R.id.viewStateStart)
            vSimple.stateSave(R.id.viewStateEnd).alpha(0f)
            layDetail.stateSave(R.id.viewStateStart)
            layDetail.stateSave(R.id.viewStateEnd).scaleX(1F).scaleY(1F).alpha(1F)
        }
    }

    fun set(amount: Int, limit: Int, expireTime: String) {
        vSimple.text = "领￥$amount"

        vDetail1.text = "￥$amount"
        vDetail2.text = "满$limit 可用"
        vDetail3.text = "有效期至$expireTime"
    }

    override fun onAnimationUpdate(tag1: Int, tag2: Int, p: Float) {
        arrayOf(vBorder1, vBorder2, vSimple, layDetail).forEach { it.stateRefresh(tag1, tag2, p) }
    }
}