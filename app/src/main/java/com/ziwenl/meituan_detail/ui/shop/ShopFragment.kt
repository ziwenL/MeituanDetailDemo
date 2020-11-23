package com.ziwenl.meituandemo.ui.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.ui.shop.ScrollableViewProvider
import kotlinx.android.synthetic.main.store_details_shop_fragment.*

/**
 * PackageName : com.ziwenl.meituandemo.ui.store
 * Author : Ziwen Lan
 * Date : 2020/9/11
 * Time : 10:18
 * Introduction :商家
 */
class ShopFragment : Fragment(), ScrollableViewProvider {

    companion object {
        fun getInstance(): Fragment {
            return ShopFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.store_details_shop_fragment, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getScrollableView(): View {
        return sv_main
    }
}