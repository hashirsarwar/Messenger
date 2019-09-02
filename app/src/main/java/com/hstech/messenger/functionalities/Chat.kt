package com.hstech.messenger.functionalities

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hstech.messenger.models.User
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.net.Uri.withAppendedPath
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


open class Chat {

    companion object
    {
        fun setIsOnlineListener(user: User)
        {
            FirebaseDatabase.getInstance()
                .reference.child("user").child(user.phone)
                .addValueEventListener(object : ValueEventListener
                {
                    override fun onDataChange(p0: DataSnapshot)
                    {
                        class Obj { lateinit var isOnline: String }

                        val obj = p0.getValue(Obj::class.java)
                        obj?.let {
                            user.isOnline = it.isOnline.toBoolean()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {}
                })
        }
    }
}