package com.hstech.messenger.functionalities

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hstech.messenger.models.User

abstract class Chat {

    fun setIsOnlineListener(user: User) {
        FirebaseDatabase.getInstance()
            .reference.child("user").child(user.phone)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    class Obj() { lateinit var isOnline: String }
                    val obj = p0.getValue(Obj::class.java)
                    obj?.let {
                        user.isOnline = it.isOnline.toBoolean()
                    }
                }
                override fun onCancelled(p0: DatabaseError) {}
            })
    }
}