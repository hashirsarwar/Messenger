package com.hs.messenger.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hs.messenger.functionalities.AppUtils

class StartServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent)
    {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED)
        {
            AppUtils.startConnectionNotifier()
        }
    }
}
