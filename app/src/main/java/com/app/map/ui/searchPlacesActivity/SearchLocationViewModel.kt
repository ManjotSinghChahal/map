package com.app.map.ui.searchPlacesActivity

import android.app.Activity
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.map.baseClasses.App
import com.app.map.data.local.AppEntities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchLocationViewModel : ViewModel() {
    var locationUpdate  = MutableLiveData<Boolean>()
    var currentSelectedLocation = MutableLiveData<AppEntities>()
    var liveLocationList  = MutableLiveData<List<AppEntities>>()

    fun onBackPress(view : View) {
        (view.context as Activity).onBackPressed()
    }

    fun saveLocation(){
        CoroutineScope(Dispatchers.IO).launch {
           try {
               val records = App.db.locationDao().insert(currentSelectedLocation.value)
               withContext(Dispatchers.Main) {
                   locationUpdate.postValue(true)
               }
           } catch (e: Exception){
                   locationUpdate.postValue(false)
           }
        }
    }
}