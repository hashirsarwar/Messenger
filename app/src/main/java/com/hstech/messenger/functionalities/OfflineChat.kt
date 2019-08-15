package com.hstech.messenger.functionalities

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import android.content.ContentResolver
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class OfflineChat: Chat() {
    companion object
    {
        fun receiveSms(context: Context?, intent: Intent?)
        {
            val dataBundle = intent?.extras
            dataBundle?.let {
                val pdus = dataBundle.get("pdus") as Array<*>
                if (pdus.isNotEmpty()) {
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    val sb = StringBuilder()
                    for (i in pdus.indices) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            val format = dataBundle.getString("format")
                            messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                        }
                        else
                            messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        sb.append(messages[i]?.messageBody)
                    }
                    val sender = messages[0]?.originatingAddress
                    val message = sb.toString()
                    Toast.makeText(context, "$message----$sender" , Toast.LENGTH_SHORT).show()
                }
            }
        }
        private lateinit var contentObserver: ContentObserver
        fun receiveMms(context: Context?, intent: Intent?)
        {
            val mmsUri = Uri.parse("content://mms/")
            var repeatCount = 0
            val cr = context?.contentResolver
            val looper = Looper.myLooper()
            looper?.let {
                val handler = Handler(looper)
                contentObserver = object : ContentObserver(handler)
                {
                    override fun onChange(selfChange: Boolean) {
                        repeatCount++
                        if (repeatCount == 2)
                        {
                            val cursor = context?.contentResolver
                                ?.query(mmsUri, null, "msg_box = 1", null, "_id")
                            if (cursor != null && cursor.count > 0)
                            {
                                cursor.moveToLast()
                                val subject = cursor.getString(cursor.getColumnIndex("sub"))
                                val id = cursor.getString(cursor.getColumnIndex("_id"))
                                val imgData: ByteArray? = null
                                val message = ""
                                val address = ""
                                val fileName = ""
                                val fileType = ""
                                val direction = ""

                                val mmsPartUri = Uri.parse("content://mms/part")
                                val mmsPartCursor = context.contentResolver.query(mmsPartUri, null, "mid = $id", null, "_id")
                                if (mmsPartCursor != null && mmsPartCursor.count > 0)
                                {
                                    mmsPartCursor.moveToLast()
                                    do
                                    {
                                        val contentType = mmsPartCursor.getString(mmsPartCursor.getColumnIndex("ct"))
                                        val partId = mmsPartCursor.getString(mmsPartCursor.getColumnIndex("_id"))
                                        Log.i("AAA", contentType)

                                    } while (mmsPartCursor.moveToPrevious())

                                    mmsPartCursor.close()
                                }
                                cursor.close()
                            }
                            cr?.unregisterContentObserver(contentObserver)
                        }
                    }
                }
                cr?.registerContentObserver(mmsUri, true, contentObserver)
            }
        }

        private fun readMMSPart(context: Context? ,partId: String): ByteArray? {
            val partData: ByteArray?
            val partURI = Uri.parse("content://mms/part/$partId")
            val baos = ByteArrayOutputStream()
            var inputStream: InputStream? = null
            try
            {
                val mContentResolver = context?.contentResolver
                inputStream = mContentResolver?.openInputStream(partURI)
                inputStream?.let {
                    val buffer = ByteArray(256)
                    var len = inputStream.read(buffer)
                    while (len >= 0) {
                        baos.write(buffer, 0, len)
                        len = inputStream.read(buffer)
                    }
                }
                partData = baos.toByteArray()
            }
            finally
            {
                inputStream?.close()
            }
            return partData
        }
    }
}