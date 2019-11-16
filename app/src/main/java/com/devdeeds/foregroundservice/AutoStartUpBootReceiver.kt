package com.devdeeds.foregroundservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build


/** @author Jayakrishnan P.M
 * Created by Devdeeds.com on 21/3/18.
 */

class AutoStartUpBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, FService::class.java))
            } else {
                context.startService(Intent(context, FService::class.java))
            }
        }
    }
}