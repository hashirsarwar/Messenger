package com.hstech.messenger.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hstech.messenger.functionalities.OfflineChat

class MMSListener : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        OfflineChat.receiveMms()
    }
}