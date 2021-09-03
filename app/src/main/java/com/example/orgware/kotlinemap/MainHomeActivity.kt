package com.example.orgware.kotlinemap

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.orgware.kotlinapicall.base.BaseActivity
import com.example.orgware.kotlinemap.ui.four.FourActivity
import com.example.orgware.kotlinemap.ui.geomap.GeoActivity
import com.example.orgware.kotlinemap.ui.one.OneActivity
import com.example.orgware.kotlinemap.ui.tthree.ThreeActivity
import com.example.orgware.kotlinemap.ui.two.TwoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainHomeActivity : BaseActivity(), View.OnClickListener{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        option.setOnClickListener(this)
        option1.setOnClickListener(this)
        option2.setOnClickListener(this)
        option3.setOnClickListener(this)
        option4.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.option -> {
                val intent = Intent(this, OneActivity::class.java)
                startActivity(intent)
            }
            R.id.option1 -> {
                val intent = Intent(this, TwoActivity::class.java)
                startActivity(intent)
            }
            R.id.option2 -> {
                val intent = Intent(this, ThreeActivity::class.java)
                startActivity(intent)
            }
            R.id.option3 -> {
                val intent = Intent(this, FourActivity::class.java)
                startActivity(intent)
            }
            R.id.option4 -> {
                val intent = Intent(this, GeoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}