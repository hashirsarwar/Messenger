package com.hstech.messenger.functionalities

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log

class ContactUtils
{
    companion object
    {
        fun getVcfFromContact(contactUri: Uri): ByteArray?
        {
            val cursor =
                AppUtils.context.contentResolver.query(contactUri, null, null, null, null)
            if (cursor != null)
            {
                cursor.moveToFirst()
                val lookupKey =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                cursor.close()
                val uri =
                    Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey)
                val fd: AssetFileDescriptor?
                try
                {
                    fd = AppUtils.context.contentResolver.openAssetFileDescriptor(uri, "r")
                    if (fd != null)
                    {
                        val fis = fd.createInputStream()
                        val buf = ByteArray(2000)
                        fis.read(buf)
                        return buf
                    }
                }
                catch (e1: Exception)
                {
                    e1.printStackTrace()
                }
            }
            return null
        }

        fun getContactsList(c: Context)
        {
            val projection = arrayOf(
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            )
            val phones = c.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            var lastPhoneName = ""
            phones?.let {
                if (it.count > 0)
                {
                    while (it.moveToNext())
                    {
                        val name = it.getString(0)
                        val phoneNumber = it.getString(1)
                        val photoUri = it.getString(2)
                        if (!name.equals(lastPhoneName, ignoreCase = true))
                        {
                            lastPhoneName = name
                            Log.d(
                                "getContactsList",
                                "$name---$phoneNumber---$photoUri")
                        }
                    }
                }
            }
            phones?.close()
        }
    }
}