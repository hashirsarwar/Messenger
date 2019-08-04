package com.hstech.messenger.functionalities

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionUtils {

    companion object
    {
        fun askPermission(activity: Activity, permission: String, reqCode: Int): Boolean {
            return if (ActivityCompat.checkSelfPermission(activity.applicationContext, permission) == PackageManager.PERMISSION_GRANTED)
                true
            else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), reqCode)
                false
            }
        }

        fun makeDefaultApp(context: Context)
        {

        }
    }
}