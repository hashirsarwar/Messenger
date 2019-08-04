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
        PermissionUtils.askPermission(this, Manifest.permission.RECEIVE_SMS, 999)
    }
}
