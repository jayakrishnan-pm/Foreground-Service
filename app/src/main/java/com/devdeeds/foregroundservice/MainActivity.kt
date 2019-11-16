package com.devdeeds.foregroundservice

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


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


        //Show data sent from service to view on click
        if (intent != null && intent.getStringExtra(FService.KEY_DATA) != null) {
            findViewById<TextView>(R.id.txtValueView).text = intent.getStringExtra(FService.KEY_DATA)
        }
    }
}
