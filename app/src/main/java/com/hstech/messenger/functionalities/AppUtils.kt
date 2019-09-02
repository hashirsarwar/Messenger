package com.hstech.messenger.functionalities

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import com.hstech.messenger.listeners.ConnectionNotifier

class AppUtils : Application()
{
    companion object
    {
        @SuppressLint("StaticFieldLeak")
        @Volatile lateinit var context: Context

        fun startConnectionNotifier()
        {
            context.startService(Intent(context, ConnectionNotifier::class.java))
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        //BELOW IS THE CODE EXECUTED ON THE START OF THE APPLICATION
        //startConnectionNotifier()
    }
}