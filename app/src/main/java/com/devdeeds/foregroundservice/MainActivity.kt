package com.devdeeds.foregroundservice

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import countdown.event.utility.app.services.FService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Start service
        if (!FService.IS_RUNNING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(applicationContext, FService::class.java))
            } else {
                startService(Intent(applicationContext, FService::class.java))
            }
        }
    }
}
