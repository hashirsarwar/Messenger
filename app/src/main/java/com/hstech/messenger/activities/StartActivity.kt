package com.hstech.messenger.activities

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

        PermissionUtils.askToRunInBackground(this)
    }
}