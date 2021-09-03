package com.example.orgware.kotlinemap.ui.relam

import com.example.orgware.kotlinapicall.base.LoadDataView
import com.example.orgware.kotlinemap.respones.gird.GirdResponse

interface GirdView: LoadDataView {
    fun onSucess(girdResponse: GirdResponse)
    fun onFailure(message: String)
}