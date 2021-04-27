package com.example.notificationtutorial

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.notificationtutorial.models.TokenModel
import com.example.notificationtutorial.utils.NotificationConstants
import com.example.notificationtutorial.utils.NotificationInterface
import com.example.notificationtutorial.utils.RetrofitConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.data.let {
            sendNotification(
                it["title"] ?: "No title",
                it["body"] ?: "No body",
                it["icon"]?.toInt() ?: 0
            );
        }

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        val retrofitClient = RetrofitConfig.getRetrofitInstance(NotificationConstants.URL)

        val endpoint = retrofitClient.create(NotificationInterface::class.java)
        val callback = endpoint.registerToken(TokenModel(token))

        callback.enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                print("fail")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    Log.d("tag", response.message())
                } else if (response.code() == 401) {

                }

            }
        })

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.


    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(title: String, messageBody: String, icon: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, NotificationConstants.CHANNEL_REMOTE_NOTIFICATION_ID)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        if (icon == 0) {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_stat_ic_notification)
        }


        with(NotificationManagerCompat.from(this)) {
            notify(0, notificationBuilder.build())
        }
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}