package com.hstech.messenger.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hstech.messenger.functionalities.AppUtils

class StartServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AppUtils.startConnectionNotifier()
    }
}
