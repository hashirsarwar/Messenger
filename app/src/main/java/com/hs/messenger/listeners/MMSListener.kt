package com.hs.messenger.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hs.messenger.functionalities.OfflineChat

class MMSListener : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        OfflineChat.receiveMms()
    }
}