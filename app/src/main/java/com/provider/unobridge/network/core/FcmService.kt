package com.provider.unobridge.network.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.ui.activities.MainActivity


class FcmService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("device_token", token)
        Prefs.init().deviceToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("RemoteMessage", remoteMessage.data.toString())
        /*if (Prefs.init().isLogIn == "true" && Prefs.init().isProfileCompleted == "true") {
            createNotificationChannel(
                remoteMessage,
                remoteMessage.notification?.title
            )
        }*/
    }


    private fun createNotificationChannel(
        remoteMessage: RemoteMessage?,
        title: String?,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.new_channel_name)
            val description = getString(R.string.new_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(getString(R.string.channel_id), name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }

        val notifyIntent = Intent(
            this,
            MainActivity::class.java
        )
        notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP


        val notificationManager = NotificationManagerCompat.from(this)

        var data: Map<String, String>? = null
        var notificationId = System.currentTimeMillis().toInt()
        data = remoteMessage?.data

       /* println("datatierpewr = ${data}")
        var otherUSerId =
            JSONObject(JSONObject(data).get("data").toString()).get("from_user_id").toString()
        var type =
            JSONObject(JSONObject(data).get("data").toString()).get("noti_type").toString()
        var hangoutId =
            JSONObject(JSONObject(data).get("data").toString()).get("hangout_id").toString()
        var likeMode =
            JSONObject(JSONObject(data).get("data").toString()).get("like_mode").toString()


        println("otherUSerId = ${otherUSerId}")
//        notificationId = data!!["random_number"].toString().toInt()
        notifyIntent.putExtra(getString(R.string.id), otherUSerId)
        notifyIntent.putExtra(getString(R.string.type_here), type)
        notifyIntent.putExtra(getString(R.string.hangout_id), hangoutId)
        notifyIntent.putExtra(getString(R.string.like_mode), likeMode)
*/

        val pIntent: PendingIntent = PendingIntent.getActivity(
            this, notificationId, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText(remoteMessage?.notification?.body)
            .setContentTitle(remoteMessage?.notification?.title)
            .setContentIntent(pIntent)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
//            .setColor(ContextCompat.getColor(baseContext,R.color.colorPrimary))
        val inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("")

        notificationManager.notify(notificationId, mBuilder.build())
    }


}

