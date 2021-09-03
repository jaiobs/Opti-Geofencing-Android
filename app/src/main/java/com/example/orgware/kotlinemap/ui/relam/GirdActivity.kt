package com.example.orgware.kotlinemap.ui.relam

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.example.orgware.kotlinapicall.base.BaseActivity
import com.example.orgware.kotlinemap.R
import com.example.orgware.kotlinemap.respones.gird.GirdResponse
import com.example.orgware.kotlinemap.respones.gird.ServicesItem
import kotlinx.android.synthetic.main.activity_grid.*

class GirdActivity : BaseActivity(), GirdView,GirdAdapter.ClickManager {


    var girdPresenter: GirdPresenter? = GirdPresenter()
    var girdAdapter:GirdAdapter?=null
    var serviceItem:List<ServicesItem>?=null


    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_grid)
        girdPresenter!!.setView(this)
        girdPresenter!!.gird()

        rv_girdlist.layoutManager= GridLayoutManager(this,3)
        girdAdapter= GirdAdapter(this,this)
        rv_girdlist.adapter=girdAdapter


    }

    override fun onSucess(girdResponse: GirdResponse) {
        girdAdapter!!.setAdapterList(girdResponse.services as ArrayList<ServicesItem>?)
    }

    override fun onFailure(message: String) {

    }
    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showRetry() {
    }

    override fun hideRetry() {
    }

    override fun showError(message: String) {
    }

    override fun context(): Context {
        return this
    }
}