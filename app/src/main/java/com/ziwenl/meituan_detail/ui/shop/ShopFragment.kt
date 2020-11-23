package com.ziwenl.meituandemo.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.ui.shop.ScrollableViewProvider
import kotlinx.android.synthetic.main.fragment_shop_details_shop.*

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
        return inflater.inflate(R.layout.fragment_shop_details_shop, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getScrollableView(): View {
        return sv_main
    }
}