package com.hs.messenger.functionalities

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class MessageDecoder {

    companion object
    {
        fun convertToImage(data: ByteArray?): Bitmap?
        {
            data?.let {
                return BitmapFactory.decodeByteArray(it, 0, it.size)
            }
            return null
        }
    }
}