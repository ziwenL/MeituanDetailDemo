package com.ziwenl.meituandemo.ui.store.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituandemo.bean.MenuChildBean
import kotlinx.android.synthetic.main.item_shop_details_menu_right.view.*

/**
 * PackageName : com.ziwenl.meituandemo.ui.store.adapter
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 16:26
 * Introduction :
 */
class MenuRightAdapter (private val data:MutableList<MenuChildBean>) : RecyclerView.Adapter<MenuRightAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_shop_details_menu_right,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.view_bottom.visibility = if (position == data.size - 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}