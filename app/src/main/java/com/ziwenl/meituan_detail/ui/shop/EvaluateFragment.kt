package com.ziwenl.meituandemo.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.ui.shop.ScrollableViewProvider
import kotlinx.android.synthetic.main.fragment_store_details_evaluate.*

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
        return inflater.inflate(R.layout.fragment_store_details_evaluate, null)
    }

    override fun getScrollableView(): View {
        return sv_main
    }
}