package com.hstech.messenger.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hstech.messenger.functionalities.OfflineChat

class OldSMSListener: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        OfflineChat.receiveSms(p0, p1)
    }
}