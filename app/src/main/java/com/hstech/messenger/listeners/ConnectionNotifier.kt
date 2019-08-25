package com.hstech.messenger.listeners

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.nkzawa.socketio.client.IO

//Notifies your connection status to the socket.io server.

class ConnectionNotifier : Service()
{
    companion object
    {
        private const val PHONE: String = "111" //TODO: change it to original phone number
        private const val SERVER_URI = "https://calm-springs-74673.herokuapp.com"
    }

    override fun onCreate()
    {
        val options = IO.Options()
        options.timeout = 904783758347024
        options.query = "phone=$PHONE"
        val socket = IO.socket(SERVER_URI, options)
        socket.connect()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder?
    {
        return null
    }
}