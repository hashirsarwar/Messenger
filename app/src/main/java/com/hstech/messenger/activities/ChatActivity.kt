package com.hstech.messenger.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.mms.APN
import com.hstech.messenger.R
import com.hstech.messenger.functionalities.OfflineChat

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val apnProjection = arrayOf("mmsc", "mmsproxy", "mmsport")
        val apnCursor = contentResolver.query(
            Uri.withAppendedPath(Uri.parse("content://telephony/carriers"), "current"),
            apnProjection, null, null, null)
        apnCursor?.let {
            it.moveToLast()
            val mmsc = it.getString(it.getColumnIndex("mmsc"))
            val mmsProxy = it.getString(it.getColumnIndex("mmsproxy"))
            val mmsPort = it.getString(it.getColumnIndex("mmsport"))

            val apn = APN()
            apn.MMSProxy = mmsProxy
            apn.MMSPort = mmsPort
            apn.MMSCenterUrl = mmsc
            apnCursor.close()
            OfflineChat.sendMessage(apn)
        }
    }
}
