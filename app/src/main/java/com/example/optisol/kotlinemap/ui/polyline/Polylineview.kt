package com.example.optisol.kotlinemap.ui.polyline

import com.optisol.optigeofencingandroid.base.LoadDataView
import com.optisol.optigeofencingandroid.respones.polyline.MapResponse

interface Polylineview : LoadDataView {
     fun onSucess(mapResponse: MapResponse)
     fun onFailure(Messages: String)
}