package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import com.boatit.boatsharing.R
import com.boatit.boatsharing.application.MainApplication
import com.boatit.boatsharing.utils.AppConstants
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class GoogleDirectionsApi {
    suspend fun getRoute(origin: LatLng, destination: LatLng): RouteData? {
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&key=${MainApplication.appContext.getString(R.string.mapAPiKey)}"

        return withContext(Dispatchers.IO) {
            try {
                val response = URL(url).readText()
                parseRoute(response)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun parseRoute(json: String): RouteData? {
        val jsonObj = JSONObject(json)
        val routes = jsonObj.getJSONArray("routes")
        if (routes.length() == 0) return null

        val route = routes.getJSONObject(0)
        val legs = route.getJSONArray("legs").getJSONObject(0)
        val duration = legs.getJSONObject("duration").getString("text")
        val polyline = route.getJSONObject("overview_polyline").getString("points")

        return RouteData(decodePolyline(polyline), duration)
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1F shl shift)
                shift += 5
            } while (b >= 0x20)
            lat += if (result and 1 != 0) result.inv() shr 1 else result shr 1

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1F shl shift)
                shift += 5
            } while (b >= 0x20)
            lng += if (result and 1 != 0) result.inv() shr 1 else result shr 1

            poly.add(LatLng(lat / 1E5, lng / 1E5))
        }
        return poly
    }
}

data class RouteData(val polylinePoints: List<LatLng>, val estimatedTime: String)
