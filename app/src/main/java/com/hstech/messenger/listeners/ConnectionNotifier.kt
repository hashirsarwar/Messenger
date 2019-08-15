package com.hstech.messenger.listeners

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import io.socket.client.IO
import io.socket.client.Socket
import android.os.PowerManager

//Notifies your connection status to the socket.io server.

class ConnectionNotifier : Service() {
    companion object {
        private lateinit var socket: Socket
        private const val PHONE: String = "111" //TODO: change it to original phone number
        private const val SERVER_URI = "" //TODO: enter the server uri
    }

    override fun onCreate() {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Messenger:ConnectionNotifier")
        wl.acquire(30*60*1000L /*30 minutes*/)
        val options = IO.Options()
        options.query = "phone=$PHONE"
        socket = IO.socket(SERVER_URI, options)
        socket.connect()
        wl.release()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        socket.disconnect()
        sendBroadcast(Intent("com.messenger.startagain"))
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
