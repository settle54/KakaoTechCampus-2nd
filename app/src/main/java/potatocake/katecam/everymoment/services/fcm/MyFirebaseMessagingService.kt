package potatocake.katecam.everymoment.services.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.repository.UserRepository
import potatocake.katecam.everymoment.presentation.view.main.MainActivity
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val userRepository = UserRepository()
    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "New token: $token")

        saveTokenToLocalStorage(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM Message", "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.getData()
            val title = data["title"]
            val body = data["body"]
            val type = data["type"]
            val targetId = data["targetId"]
            Log.d("FCM Message", "Message data payload: ${remoteMessage.data}")
        }

        remoteMessage.notification?.let {
            val title = it.title
            val body = it.body
            Log.d("FCM Message", "Message Notification Body: ${it.body}")

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            createNotificationChannel()
            setNotification(title, body)
        }
    }

    private fun saveTokenToLocalStorage(token: String) {
        val prefs = applicationContext.getSharedPreferences("FCM_PREFS", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()

        // pending 상태도 저장
        prefs.edit().putBoolean("token_needs_sync", true).apply()
    }

    private fun setNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title ?: "[중요] 포그라운드 알림")
            .setContentText(body ?: "앱이 실행 중입니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        val descriptionText = getString(R.string.fcm_channel_description)
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 111111
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
    }
}