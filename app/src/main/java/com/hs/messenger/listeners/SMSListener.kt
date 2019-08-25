package com.hs.messenger.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hs.messenger.functionalities.OfflineChat

class SMSListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        OfflineChat.receiveSms(intent)
    }
}