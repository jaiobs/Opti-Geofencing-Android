package com.example.orgware.kotlinemap.ui.polyline

import com.example.orgware.kotlinemap.base.LoadDataView
import com.example.orgware.kotlinemap.respones.polyline.MapResponse

interface Polylineview : LoadDataView {
     fun onSucess(mapResponse: MapResponse)
     fun onFailure(Messages: String)
}