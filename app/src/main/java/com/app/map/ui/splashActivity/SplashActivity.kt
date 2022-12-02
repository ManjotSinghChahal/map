package com.app.map.ui.splashActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.app.map.R
import com.app.map.ui.locationListActivity.LocationListActivity
import com.app.map.utils.Constants
import com.app.map.utils.moveToScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)
        moveToLocationScreen()
    }

    private fun moveToLocationScreen() {
        Handler(Looper.myLooper()!!).postDelayed({
            this.moveToScreen(LocationListActivity(),true)
        },Constants.SPLASH_TIME)
    }

}
