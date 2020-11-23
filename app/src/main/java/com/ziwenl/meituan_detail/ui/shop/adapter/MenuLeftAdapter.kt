package com.ziwenl.meituandemo.ui.store.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituandemo.bean.MenuTabBean
import kotlinx.android.synthetic.main.item_shop_details_menu_left.view.*

/**
 * PackageName : com.ziwenl.meituandemo.ui.store.adapter
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 15:31
 * Introduction :
 */
class MenuLeftAdapter(
    private val data: MutableList<MenuTabBean>
) :
    RecyclerView.Adapter<MenuLeftAdapter.ViewHolder>() {

    var currentPosition = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop_details_menu_left, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.cb_name.isChecked = position == currentPosition
        holder.itemView.cb_name.text = data[position].name
        holder.itemView.cb_name.setOnClickListener {
            mCallBack?.onClickItem(position)
        }
        holder.itemView.view_bottom.visibility = if (position == data.size - 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private var mCallBack: Callback? = null

    fun setCallback(callback: Callback) {
        mCallBack = callback
    }

    interface Callback {
        fun onClickItem(position: Int)
    }
}