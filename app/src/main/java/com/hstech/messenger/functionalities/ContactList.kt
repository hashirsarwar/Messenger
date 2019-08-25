package com.hstech.messenger.functionalities

import android.content.Context
import android.provider.ContactsContract
import android.util.Log

class ContactList {

    companion object {

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