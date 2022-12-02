package com.app.map.ui.locationListActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.map.baseClasses.App
import com.app.map.data.local.AppEntities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationViewModel : ViewModel() {
    var liveLocationList  = MutableLiveData<List<AppEntities>>()
    var isLocFetchingFailed = MutableLiveData<Boolean>()

    fun fetchAllLocations(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val records: List<AppEntities> = App.db.locationDao().fetchAllLocations()
                withContext(Dispatchers.Main) {
                    liveLocationList.postValue(records)
                }
            }catch (e: Exception){
                isLocFetchingFailed.postValue(true)
            }
        }
    }
    fun deleteLocation(id: String){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                App.db.locationDao().delete(id)
                fetchAllLocations()
            } catch (e: Exception){
                isLocFetchingFailed.postValue(true)
            }
        }
    }
}