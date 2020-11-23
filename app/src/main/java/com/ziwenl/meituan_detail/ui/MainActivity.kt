package com.ziwenl.meituan_detail.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ziwenl.meituan_detail.R
import com.ziwenl.meituan_detail.ui.shop.ShopDetailsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_open.setOnClickListener {
            startActivity(Intent(this, ShopDetailsActivity::class.java))
        }
    }
}