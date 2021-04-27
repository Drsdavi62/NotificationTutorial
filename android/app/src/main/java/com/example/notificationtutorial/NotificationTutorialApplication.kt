package com.example.notificationtutorial

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.notificationtutorial.utils.NotificationConstants

class NotificationTutorialApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelLocal = NotificationChannel(
                NotificationConstants.CHANNEL_LOCAL_NOTIFICATION_ID,
                "Local Notifications",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Local Notification Description"
            }

            val channelRemote = NotificationChannel(
                NotificationConstants.CHANNEL_REMOTE_NOTIFICATION_ID,
                "Remote Notifications",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Remote Notification Description"
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelLocal)
            notificationManager.createNotificationChannel(channelRemote)
        }
    }
}