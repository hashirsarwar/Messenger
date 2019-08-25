package com.hs.messenger.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hs.messenger.R
import com.hs.messenger.functionalities.PermissionUtils

class StartActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        PermissionUtils.askToRunInBackground(this)
    }
}