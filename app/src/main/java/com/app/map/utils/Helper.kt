package com.app.map.utils

import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import com.app.map.BuildConfig
import com.app.map.ui.searchPlacesActivity.SearchLocationActivity
import com.directions.route.AbstractRouting
import com.directions.route.Route
import com.directions.route.Routing
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.model.Place
import java.util.*

object Helper {

    fun getAddress(context: Context, place: Place) : String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(place.latLng!!.latitude, place.latLng!!.longitude, 1)
        return addresses[0].locality
    }

    fun addMapMarker(lat: Double, lng: Double, googleMap: GoogleMap, isAnimate: Boolean = true){
        googleMap.addMarker(MarkerOptions().position(LatLng(lat,lng)))
        if (isAnimate)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 10.0f))
    }

     fun getPath(start: LatLng?, end: LatLng?,context: SearchLocationActivity) {
        if (start != null && end != null) {
            val routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(context)
                .alternativeRoutes(true)
                .waypoints(start, end)
                .key(BuildConfig.MAPS_API_KEY)
                .build()
            routing.execute()
        }
    }

    fun drawPolyLines(route: ArrayList<Route>?, shortestRouteIndex: Int, googleMap: GoogleMap?) {
        val polylines = ArrayList<Polyline>()
        val polyOptions = PolylineOptions()
        if (route != null){
            for (i in route.indices) {
                if (i == shortestRouteIndex) {
                    polyOptions.color(Color.RED)
                    polyOptions.width(7f)
                    polyOptions.addAll(route[shortestRouteIndex].points)
                    val polyline: Polyline = googleMap!!.addPolyline(polyOptions)
                    (polylines).add(polyline)
                }
            }
        }

    }
 }