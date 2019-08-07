package com.hstech.messenger.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hstech.messenger.R
import com.hstech.messenger.functionalities.PermissionUtils

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        PermissionUtils.askPermission(this, Manifest.permission.READ_SMS, 888)
//        val mmsUri = Uri.parse("content://mms/")
//        val cursor = contentResolver
//            .query(mmsUri, null, "msg_box = 1", null, "_id")
//
//        if (cursor != null && cursor.count > 0)
//        {
//            cursor.moveToLast()
//            val subject = cursor.getString(cursor.getColumnIndex("sub"))
//            val id = cursor.getString(cursor.getColumnIndex("_id"))
//            val imgData: ByteArray? = null
//            val message = ""
//            val address = ""
//            val fileName = ""
//            val fileType = ""
//            val direction = ""
//
//            val mmsPartUri = Uri.parse("content://mms/part")
//            val mmsPartCursor = contentResolver.query(mmsPartUri, null, "mid = $id", null, "_id")
//            Toast.makeText(this, mmsPartCursor?.count.toString(), Toast.LENGTH_SHORT).show()
//            mmsPartCursor?.close()
//        }
//
//        cursor?.close()
    }
}