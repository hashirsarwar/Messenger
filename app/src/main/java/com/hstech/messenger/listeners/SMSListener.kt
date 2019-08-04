package com.hstech.messenger.listeners

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SMSListener : BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "sms", Toast.LENGTH_SHORT).show()
    }
}