package com.hstech.messenger.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hstech.messenger.R
import com.hstech.messenger.functionalities.PermissionUtils

class StartActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        PermissionUtils.askPermission(this, Manifest.permission.READ_PHONE_STATE, 888)
    }

    fun sendIt(view: View)
    {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }
}