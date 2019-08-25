package com.hs.messenger.functionalities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

class CheckMyConnection {
    interface OnStateChangedListener
    {
        fun onOnline()
        fun onOffline()
    }

    companion object {

        private var avoidRepetition = 0
        private lateinit var listener: OnStateChangedListener
        private lateinit var receiver: BroadcastReceiver

        fun setOnStateChangedListener(c: Context,l: OnStateChangedListener)
        {
            listener = l
            enableBroadcastReceiver(c)
        }

        private fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm.activeNetworkInfo == null)
                return false
            else {
                val command = "ping -c 1 google.com"
                return Runtime.getRuntime().exec(command).waitFor() == 0
            }
        }

        private fun enableBroadcastReceiver(context: Context)
        {
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            receiver = object : BroadcastReceiver()
            {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    if (isNetworkConnected(context))
                    {
                        if (::listener.isInitialized)
                        {
                            if (avoidRepetition != 1)
                            {
                                listener.onOnline()
                                avoidRepetition = 1
                            }
                        }
                    }
                    else
                    {
                        if (::listener.isInitialized)
                        {
                            if (avoidRepetition != 2)
                            {
                                listener.onOffline()
                                avoidRepetition = 2
                            }
                        }
                    }
                }
            }
            context.registerReceiver(receiver, filter)
        }
    }
}