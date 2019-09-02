package com.hstech.messenger.functionalities

import android.content.Intent
import android.database.ContentObserver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.SmsMessage
import android.util.Log
import com.google.android.mms.APN
import com.hstech.messenger.R
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception
import com.hstech.messenger.models.MessageObject
import com.hstech.messenger.models.MessageContent
import com.hstech.messenger.models.MessageTypes
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Settings
import com.klinker.android.send_message.Transaction

class OfflineChat : Chat() {

    companion object
    {
        private lateinit var contentObserver: ContentObserver
        private val mmsPartUri: Uri = Uri.parse("content://mms/part")
        private val mmsUri: Uri = Uri.parse("content://mms")
        private val contentResolver = AppUtils.context.contentResolver

        fun receiveSms(intent: Intent?)
        {
            ReceiveSMSAsync().execute(intent)
        }

        fun receiveMms() {

            val looper = Looper.myLooper()

            looper?.let {

                val handler = Handler(looper)
                contentObserver = object : ContentObserver(handler)
                {
                    override fun onChange(selfChange: Boolean)
                    {
                        ReceiveMMSAsync().execute()
                    }
                }
                contentResolver.registerContentObserver(Uri.parse("content://mms/_id"), true, contentObserver)
            }
        }

        fun sendMessage(apn: APN)
        {
            val settings = Settings()
            Log.i("AAA", apn.MMSCenterUrl)
            settings.mmsc = apn.MMSCenterUrl
            settings.port = apn.MMSPort
            settings.proxy = apn.MMSProxy

            val sendTransaction = Transaction(AppUtils.context, settings)
            val msg = Message("Hello from the other side", "+923106026180")
            msg.addImage(BitmapFactory.decodeResource(AppUtils.context.resources, R.drawable.ic_launcher_background))
            sendTransaction.sendNewMessage(msg, Transaction.NO_THREAD_ID)
        }

        class ReceiveSMSAsync : AsyncTask<Intent, Void, MessageObject>()
        {
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
                        for (i in pdus.indices)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            {
                                val format = it.getString("format")
                                messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                            }
                            else
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

        class ReceiveMMSAsync : AsyncTask<Void, Void, MessageObject>()
        {
            override fun onPostExecute(result: MessageObject?)
            {
                Log.i("AAA", "MMS received!")
                //Received message object here.
            }

            override fun doInBackground(vararg p0: Void?): MessageObject
            {
                val cursor =
                    contentResolver.query(mmsUri, null, "msg_box = 1", null, "_id")
                val message = MessageObject(false)

                if (cursor != null && cursor.count > 0)
                {
                    cursor.moveToLast()
                    val id = cursor.getString(cursor.getColumnIndex("_id"))
                    val mmsPartCursor =
                        contentResolver.query(mmsPartUri, null, "mid = $id", null, "_id")
                    if (mmsPartCursor != null && mmsPartCursor.count > 0)
                    {
                        message.sender = getMMSSender(id)
                        message.time = System.currentTimeMillis()
                        mmsPartCursor.moveToLast()

                        do
                        {
                            val contentType = mmsPartCursor.getString(mmsPartCursor.getColumnIndex("ct"))
                            val partId = mmsPartCursor.getString(mmsPartCursor.getColumnIndex("_id"))

                            if (contentType.equals("text/plain", ignoreCase = true))
                            {
                                message.messageText = readMMSText(id, partId)
                            }

                            else if (!contentType.equals("application/smil", ignoreCase = true))
                            {
                                val data = readMMSPart(partId)

                                if (data != null)
                                {
                                    val type: String = when
                                    {
                                        contentType.equals("text/x-vCard", ignoreCase = true) ->
                                        {
                                            MessageTypes.CONTACT_TYPE
                                        }
                                        contentType.contains("image/", ignoreCase = true) ->
                                        {
                                            if (contentType.equals("image/gif", ignoreCase = true))
                                            {
                                                MessageTypes.GIF_IMAGE_TYPE
                                            }
                                            else
                                            {
                                                MessageTypes.SIMPLE_IMAGE_TYPE
                                            }
                                        }
                                        contentType.contains("audio/", ignoreCase = true) ->
                                        {
                                            MessageTypes.AUDIO_TYPE
                                        }
                                        contentType.contains("video/", ignoreCase = true) ->
                                        {
                                            MessageTypes.VIDEO_TYPE
                                        }
                                        else ->
                                        {
                                            MessageTypes.FILE_TYPE
                                        }
                                    }
                                    message.messageContent?.add(MessageContent(data, type))
                                }
                            }

                        } while (mmsPartCursor.moveToPrevious())

                        mmsPartCursor.close()
                    }
                    cursor.close()
                }
                contentResolver.unregisterContentObserver(contentObserver)
                return message
            }
        }

        private fun getMMSSender(mmsId: String): String?
        {
            val uriAddrPart = Uri.parse("content://mms/$mmsId/addr")
            val addrCur = AppUtils.context.contentResolver
                .query(uriAddrPart, null, "type=151", null, "_id")

            return if (addrCur != null && addrCur.count > 0)
            {
                addrCur.moveToLast()
                val address = addrCur.getString(addrCur.getColumnIndex("address"))
                addrCur.close()
                address
            }
            else null
        }

        private fun readMMSText(mmsId: String, partId: String): String?
        {
            var msg: String? = null
            val curPart1 = AppUtils.context.contentResolver
                .query(
                    mmsPartUri, null, "mid = " + mmsId +
                            " and _id =" + partId, null, "_id"
                )
            if (curPart1 != null)
            {
                curPart1.moveToLast()
                msg = curPart1.getString(curPart1.getColumnIndex("text"))
                curPart1.close()
            }
            return msg
        }

        private fun readMMSPart(partId: String): ByteArray?
        {
            var partData: ByteArray? = null
            val partURI = Uri.parse("content://mms/part/$partId")
            val baos = ByteArrayOutputStream()
            var inputStream: InputStream? = null
            try
            {
                val mContentResolver = AppUtils.context.contentResolver
                inputStream = mContentResolver.openInputStream(partURI)
                inputStream?.let {
                    val buffer = ByteArray(256)
                    var len = inputStream.read(buffer)
                    while (len >= 0)
                    {
                        baos.write(buffer, 0, len)
                        len = inputStream.read(buffer)
                    }
                }
                partData = baos.toByteArray()
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
            finally
            {
                inputStream?.close()
            }
            return partData
        }
    }
}