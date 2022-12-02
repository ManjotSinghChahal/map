package com.app.map.ui.locationListActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.map.R
import com.app.map.data.local.AppEntities
import com.app.map.databinding.ActivityLocationListBinding
import com.app.map.ui.searchPlacesActivity.SearchLocationActivity
import com.app.map.utils.Constants
import com.app.map.utils.dialog.BottomSheetSort
import com.app.map.utils.gone
import com.app.map.utils.visible

class LocationListActivity : AppCompatActivity() {
    private lateinit var viewModel: LocationViewModel
    private lateinit var binding: ActivityLocationListBinding
    private lateinit var mAdapter: LocationListAdapter
    private var locationList = ArrayList<AppEntities>()
    private var primaryData : AppEntities? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_location_list)
        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        observers()
        recyclerview()
        clickListeners()
        viewModel.fetchAllLocations()
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.fetchAllLocations()
    }

    private fun clickListeners(){
        binding.apply {
            addLocation.setOnClickListener {
                moveToNext(false)
            }
            sortLocation.setOnClickListener {
                openBottomSheet()
            }
            locationIcon.setOnClickListener {
                moveToNext(true)
            }
        }
    }

    private fun moveToNext(isMarkerRoute: Boolean) {
        val intent = Intent(this@LocationListActivity, SearchLocationActivity::class.java)
        if (isMarkerRoute){
            val bundle = Bundle()
            bundle.putParcelableArrayList(Constants.MARKER_PATH,locationList)
            intent.putExtras(bundle)
        }
        else {
           if (primaryData != null)
               intent.putExtra(Constants.ENTITY,primaryData)
        }
        startActivity(intent)
    }

    private fun openBottomSheet() {
        BottomSheetSort { isAscending: Boolean ->
            run {
                sortLocation(isAscending)
            }
        }.apply {
            show(supportFragmentManager, tag)
        }
    }

    private fun sortLocation(isAscending: Boolean) {
        val sortedList: List<AppEntities> = locationList.sorted()
        mAdapter.apply {
            if (isAscending)
                submitList(sortedList)
            else
                submitList(sortedList.reversed())
        }

    }

    private fun observers() {
        viewModel.liveLocationList.observe(this) {
            binding.apply {
                if (it.isNotEmpty()){
                    recyclerviewLoc.visible()
                    txtNoData.gone()
                }
                else {
                    recyclerviewLoc.gone()
                    txtNoData.visible()
                }
            }

            mAdapter.submitList(it)
            locationList = it as ArrayList<AppEntities>
            if (it.isNotEmpty()){
                for (pos  in it.indices){
                    if (it[pos].primary!! == 1){
                        primaryData = it[pos]
                        break
                    }
                }

            }
        }
        viewModel.isLocFetchingFailed.observe(this) {
          // failed
        }
    }

    private fun recyclerview() {
        mAdapter = LocationListAdapter(this, object : LocationListAdapter.LocationCallback {
            override fun onLocationEdit(item: AppEntities) {
                val intent = Intent(this@LocationListActivity, SearchLocationActivity::class.java)
                intent.putExtra(Constants.EXISTING_DATA,item)
                intent.putExtra(Constants.ENTITY,primaryData)
                startActivity(intent)
            }
            override fun onLocationDeleted(item: AppEntities) {
                viewModel.deleteLocation(item.id)
            }
        })
        val lManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerviewLoc.apply {
            layoutManager = lManager
            mAdapter.submitList(ArrayList<AppEntities>())
            adapter = mAdapter
        }
    }
}