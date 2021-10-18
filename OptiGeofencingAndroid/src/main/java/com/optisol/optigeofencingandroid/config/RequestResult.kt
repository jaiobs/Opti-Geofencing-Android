package com.example.optisol.kotlinemap.config

class RequestResult {

    companion object{
        const val OK = "OK"
        const val NOT_FOUND = "NOT_FOUND"
        const val ZERO_RESULTS = "ZERO_RESULTS"
        const val MAX_WAYPOINTS_EXCEEDED = "MAX_WAYPOINTS_EXCEEDED"
        const val INVALID_REQUEST = "INVALID_REQUEST"
        const val OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT"
        const val REQUEST_DENIED = "REQUEST_DENIED"
        const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
    }
}