package com.ziwenl.meituandemo.ui.store

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.ui.shop.ScrollableViewProvider
import com.ziwenl.meituan_detail.ui.shop.adapter.FloatDecoration
import com.ziwenl.meituandemo.bean.MenuChildBean
import com.ziwenl.meituandemo.bean.MenuTabBean
import com.ziwenl.meituandemo.ui.store.adapter.MenuLeftAdapter
import com.ziwenl.meituandemo.ui.store.adapter.MenuRightAdapter
import kotlinx.android.synthetic.main.store_details_evaluate_fragment.*
import kotlinx.android.synthetic.main.store_details_menu_right_group_item.view.*

/**
 * PackageName : com.ziwenl.meituandemo.ui.store
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 10:18
 * Introduction :评价
 */
class EvaluateFragment : Fragment(), ScrollableViewProvider {

    companion object {
        fun getInstance(): EvaluateFragment {
            val fragment = EvaluateFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.store_details_evaluate_fragment, null)
    }

    override fun getScrollableView(): View {
        return sv_main
    }
}