package com.hstech.messenger.functionalities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast

class OfflineChat: Chat() {
    companion object
    {
        fun receiveSms(p0: Context?, p1: Intent?)
        {
            val dataBundle = p1?.extras
            dataBundle?.let {
                val pdus = dataBundle.get("pdus") as Array<*>
                if (pdus.isNotEmpty()) {
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    val sb = StringBuilder()
                    for (i in pdus.indices) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            val format = dataBundle.getString("format")
                            messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                        }
                        else
                            messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        sb.append(messages[i]?.messageBody)
                    }
                    val sender = messages[0]?.originatingAddress
                    val message = sb.toString()
                    Toast.makeText(p0, "$message----$sender" , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}