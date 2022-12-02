package com.app.map.ui.searchPlacesActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.map.BuildConfig
import com.app.map.R
import com.app.map.data.local.AppEntities
import com.app.map.databinding.ActivitySearchLocationBinding
import com.app.map.utils.Constants
import com.app.map.utils.Helper.addMapMarker
import com.app.map.utils.Helper.drawPolyLines
import com.app.map.utils.Helper.getAddress
import com.app.map.utils.Helper.getPath
import com.app.map.utils.gone
import com.app.map.utils.visible
import com.directions.route.Route
import com.directions.route.RouteException
import com.directions.route.RoutingListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.SphericalUtil


class SearchLocationActivity : AppCompatActivity(), OnMapReadyCallback, RoutingListener {
    private lateinit var viewModel: SearchLocationViewModel
    private lateinit var binding: ActivitySearchLocationBinding
    var mapFragment: SupportMapFragment? = null
    var googleMap: GoogleMap? = null
    var primaryData : AppEntities? = null
    var existingData : AppEntities? = null
    var allLocation : ArrayList<AppEntities>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_location)
        viewModel = ViewModelProvider(this)[SearchLocationViewModel::class.java]
        initialize()
        initilizeMap()
        initPlaces()
        observers()
        clickListeners()
    }

    private fun initialize(){
        val intent = intent.extras
        if (intent != null && intent.containsKey(Constants.EXISTING_DATA)) {
            existingData = intent.getParcelable<AppEntities>(Constants.EXISTING_DATA) as AppEntities
        }
        if (intent != null && intent.containsKey(Constants.ENTITY)) {
            primaryData = intent.getParcelable<AppEntities>(Constants.ENTITY) as AppEntities
        }
        if (intent != null && intent.containsKey(Constants.MARKER_PATH)) {
            allLocation = intent.getParcelableArrayList(Constants.MARKER_PATH)!!
        }
    }

    private fun clickListeners(){
        binding.apply {
            saveLocation.setOnClickListener {
                viewModel.saveLocation()
            }
            searchLocation.setOnClickListener {
                val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG)
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this@SearchLocationActivity)
                resultLauncher.launch(intent)
            }

        }
    }

    private fun initilizeMap() {
        if (googleMap == null) {
            mapFragment = supportFragmentManager.findFragmentById(R.id.search_map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this)
        }
    }

    private fun initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY)
        }
    }

    override fun onMapReady(Map: GoogleMap) {
        googleMap = Map
        if (existingData != null) {
                addMapMarker(existingData?.latitude!!,existingData?.longitude!!,googleMap!!)
        }
        else if (allLocation != null && allLocation!!.isNotEmpty()) {
            for (pos in 0 until allLocation!!.size){
                addMapMarker(allLocation!![pos].latitude!!,allLocation!![pos].longitude!!,googleMap!!,allLocation!![pos].primary == 1)
            }
            val sortedList: List<AppEntities> = allLocation!!.sorted()
            for ( pos in 0 until allLocation!!.size-1){
                getPath(
                    LatLng(sortedList[pos].latitude!!,sortedList[pos].longitude!!),
                    LatLng(sortedList[pos+1].latitude!!,sortedList[pos+1].longitude!!),this)
            }
        }
    }

    private fun observers() {
        viewModel.locationUpdate.observe(this) {
           onBackPressed()
        }
    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            parsePlaceData(data)
        }
    }

    private fun parsePlaceData(data: Intent?) {
        data?.let {
            val place = Autocomplete.getPlaceFromIntent(data)
            binding.searchLocation.gone()
            binding.saveLocation.visible()
            val record = AppEntities()
            record.apply {

                latitude = place.latLng!!.latitude
                longitude = place.latLng!!.longitude
                city = getAddress(this@SearchLocationActivity,place)
                address = place.address!!

                var distanceFromPrimary = 0.0
                if (primaryData!= null){
                    val calculatedDistance = SphericalUtil.computeDistanceBetween(
                        LatLng( latitude!!.toDouble(), longitude!!.toDouble()),
                        LatLng(primaryData!!.latitude!!.toDouble(), primaryData!!.longitude!!.toDouble()))
                    distanceFromPrimary = String.format("%.2f", calculatedDistance / 1000).toDouble()
                    primary = 2
                }
                else {
                    primary = 1
                }
                distance = distanceFromPrimary
            }
            if (existingData != null)
                record.id = existingData?.id!!
            else
                record.id = place.id!!

            viewModel.currentSelectedLocation.value = record
            addMapMarker(place.latLng!!.latitude, place.latLng!!.longitude,googleMap!!)
        }
    }


    override fun onRoutingFailure(p0: RouteException?) {

    }
    override fun onRoutingStart() {

    }
    override fun onRoutingSuccess(route: ArrayList<Route>?, shortestRouteIndex: Int) {
        drawPolyLines(route,shortestRouteIndex,googleMap)

    }
    override fun onRoutingCancelled() {

    }
}