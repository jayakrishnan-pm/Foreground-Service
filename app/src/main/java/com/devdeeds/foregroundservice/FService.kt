package com.devdeeds.foregroundservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat


/** @author Jayakrishnan P.M
 * Created by Devdeeds.com on 21/3/18.
 */

class FService : Service() {

    private val mHandler: Handler? = Handler()

    private lateinit var mRunnable: Runnable


    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "ON START COMMAND")

        if (intent != null) {

            when (intent.action) {

                ACTION_STOP_FOREGROUND_SERVICE -> {
                    stopService()
                }

                ACTION_OPEN_APP -> openAppHomePage(intent.getStringExtra(KEY_DATA))
            }
        }
        return START_STICKY;
    }


    private fun openAppHomePage(value: String) {

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(KEY_DATA, value)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val chan = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )

            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)

        }
    }

    /* Used to build and start foreground service. */
    private fun startForegroundService() {

        //Create Notification channel for all the notifications sent from this app.
        createNotificationChannel()

        // Start foreground service.
        startFService()


        mRunnable = Runnable {

            notifyNextEvent()
            mHandler?.postDelayed(mRunnable, ONE_MIN_MILLI)  //repeat
        };

        // Schedule the task to repeat after 1 second
        mHandler?.postDelayed(
            mRunnable, // Runnable
            ONE_MIN_MILLI // Delay in milliseconds
        )

    }

    private fun startFService() {

        val description = getString(R.string.msg_notification_service_desc)
        val title = String.format(
            getString(R.string.title_foreground_service_notification),
            getString(R.string.app_name)
        )

        startForeground(SERVICE_ID, getStickyNotification(title, description))
        IS_RUNNING = true
    }


    private fun getStickyNotification(title: String, message: String): Notification? {

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, Intent(), 0)

        // Create notification builder.
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        // Make notification show big text.
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(title)
        bigTextStyle.bigText(message)
        // Set big text style.
        builder.setStyle(bigTextStyle)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.drawable.ic_stat_name)
        //val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_alarm_on)
        //builder.setLargeIcon(largeIconBitmap)
        // Make the notification max priority.
        builder.priority = NotificationCompat.PRIORITY_DEFAULT

        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true)


        // Add Open App button in notification.
        val openAppIntent = Intent(applicationContext, FService::class.java)
        openAppIntent.action = ACTION_OPEN_APP
        openAppIntent.putExtra(KEY_DATA, "" + System.currentTimeMillis())
        val pendingPlayIntent = PendingIntent.getService(applicationContext, 0, openAppIntent, 0)
        val openAppAction = NotificationCompat.Action(
            android.R.drawable.ic_menu_view,
            getString(R.string.lbl_btn_sticky_notification_open_app),
            pendingPlayIntent
        )
        builder.addAction(openAppAction)


        // Build the notification.
        return builder.build()

    }


    private fun notifyNextEvent() {

        NotificationHelper.onHandleEvent(
            getString(R.string.title_gen_notification),
            getString(R.string.description_gen_notification),
            applicationContext
        )
    }


    private fun stopService() {
        // Stop foreground service and remove the notification.
        stopForeground(true)
        // Stop the foreground service.
        stopSelf()

        IS_RUNNING = false
    }


    override fun onDestroy() {
        IS_RUNNING = false
        mHandler?.removeCallbacks(null)
    }

    companion object {

        const val TAG = "FOREGROUND_SERVICE"


        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"

        const val ACTION_OPEN_APP = "ACTION_OPEN_APP"
        const val KEY_DATA = "KEY_DATA"

        private const val CHANNEL_ID: String = "1001"
        private const val CHANNEL_NAME: String = "Event Tracker"
        private const val SERVICE_ID: Int = 1
        private const val ONE_MIN_MILLI: Long = 60000  //1min

        var IS_RUNNING: Boolean = false
    }
}