package com.example.orgware.kotlinapicall.ui.polyline

import com.example.orgware.kotlinapicall.base.LoadDataView
import com.example.orgware.kotlinapicall.data.respones.polyline.MapResponse

interface Polylineview :LoadDataView {
     fun onSucess(mapResponse: MapResponse)
     fun onFailure(Messages: String)
}