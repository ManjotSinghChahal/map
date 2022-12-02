package com.app.map.utils

import android.app.Activity
import android.content.Intent
import android.view.View

fun Activity.moveToScreen(mClazz: Activity, clearBackStack: Boolean = false) {
    startActivity(Intent(this, mClazz::class.java))
    if (clearBackStack) {
        finishAffinity()
    }
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

