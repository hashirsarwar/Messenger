package com.hstech.messenger.functionalities
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import com.google.android.mms.pdu_alt.PduParser
//import com.google.android.mms.pdu_alt.MultimediaMessagePdu
//
//class MMSListener : BroadcastReceiver()
//{
//    override fun onReceive(p0: Context?, p1: Intent?) {
//        val data = p1?.getByteArrayExtra("data")
//        Log.i("AAA", "$data")
//        val parser = PduParser(data)
//        val pdu = parser.parse()
//        var returnData: ByteArray? = null
//        if (pdu is MultimediaMessagePdu) {
//            Log.i("AAA", "pdu multimedia")
//            val body = pdu.body
//            if (body != null) {
//                Log.i("AAA", "body is not null")
//                val partsNum = body.partsNum
//                for (i in 0 until partsNum) {
//                    try {
//                        val part = body.getPart(i)
//                        if (part == null || part.data == null || part.contentType == null || part.name == null) {
//                            continue
//                        }
//                        // Assuming image type for testing purposes
//                        val partType = String(part.contentType)
//                        Log.i("AAA", "Part type $partType")
//                        val partName = String(part.name)
//                        Log.i("AAA", "Part name $partName")
//                        returnData = part.data
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
//        Log.i("AAA", "Return data $returnData")
//    }
//}