package com.hs.messenger.functionalities

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Telephony
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.content.ComponentName
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.hs.messenger.listeners.OldSMSListener

//This class should always be called from an activity.

class PermissionUtils {

    companion object
    {
        fun askPermission(activity: Activity, permission: String, reqCode: Int): Boolean {
            return if (ActivityCompat.checkSelfPermission(AppUtils.context, permission) == PackageManager.PERMISSION_GRANTED)
                true
            else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), reqCode)
                false
            }
        }

        fun makeDefaultApp(context: Context)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (context.packageName != Telephony.Sms.getDefaultSmsPackage(context)) {
                    val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.packageName)
                    context.startActivity(intent)
                }
                disableOldSmsBroadcast(context)
            }
        }

        private fun disableOldSmsBroadcast(context: Context)
        {
            val receiver = ComponentName(context, OldSMSListener::class.java)
            val pm = context.packageManager

            pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        fun askToRunInBackground(context: Context)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                val packageName = context.packageName
                val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                if (!pm.isIgnoringBatteryOptimizations(packageName))
                {
                    val intent = Intent()
                    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    intent.data = Uri.parse("package:$packageName")
                    context.startActivity(intent)
                }
            }
        }
    }
}