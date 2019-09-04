package com.hstech.messenger.functionalities

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.telephony.SmsMessage
import com.google.android.mms.APN
import com.hstech.messenger.models.MessageObject
import com.klinker.android.send_message.Message

class OfflineChat : Chat() {

    companion object {

        fun receiveSms(intent: Intent?)
        {
            ReceiveSMSAsync().execute(intent)
        }

        fun receiveMms()
        {

        }

        fun sendMessage(msg: MessageObject, vararg addresses: String)
        {
            val message = Message()
            if (!msg.messageText.isNullOrEmpty())
            {
                message.text = msg.messageText
            }
            if (!msg.messageContent.isNullOrEmpty())
            {
                for (i in msg.messageContent!!)
                {
                    message.addMedia(i.messageBody, i.messageType)
                }
            }
        }

        //TODO: test for dual sim
        private fun getAPN(): APN?
        {
            val apnProjection = arrayOf("mmsc", "mmsproxy", "mmsport")
            val apnCursor = AppUtils.context.contentResolver.query(
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
                return apn
            }
            return null
        }

        class ReceiveSMSAsync : AsyncTask<Intent, Void, MessageObject>() {
            override fun onPostExecute(result: MessageObject?) {
                //Received message object here.
            }

            override fun doInBackground(vararg p0: Intent?): MessageObject? {

                val dataBundle = p0[0]?.extras

                dataBundle?.let {

                    val pdus = it.get("pdus") as Array<*>
                    if (pdus.isNotEmpty()) {
                        val messages = arrayOfNulls<SmsMessage>(pdus.size)
                        val sb = StringBuilder()
                        for (i in pdus.indices) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                val format = it.getString("format")
                                messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                            } else
                                messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)

                            sb.append(messages[i]?.messageBody)
                        }

                        val message = MessageObject(false)
                        message.messageContent = null
                        message.time = System.currentTimeMillis()
                        message.sender = messages[0]?.originatingAddress
                        message.messageText = sb.toString()

                        return message
                    }
                }
                return null
            }
        }

    }
}