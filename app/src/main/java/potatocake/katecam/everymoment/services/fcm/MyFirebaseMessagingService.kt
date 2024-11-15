package potatocake.katecam.everymoment.services.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.presentation.view.main.MainActivity
import potatocake.katecam.everymoment.services.notification.NotificationActionReceiver

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager
    private var targetId: String? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "New token: $token")

        saveTokenToLocalStorage(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM Message", "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.getData()
            val type = data["type"]
            targetId = data["targetId"]

            Log.d("FCM Message", "Message data payload: ${remoteMessage.data}")
            Log.d("emoji notification", "targetId: ${targetId}")

            remoteMessage.notification?.let {
                val title = it.title
                val body = it.body
                Log.d("FCM Message", "Message data payload: ${remoteMessage.data}")
                Log.d("emoji notification1", "targetId: ${targetId}")

                if (type == "MOOD_CHECK") {
                    setNotification(type, title, body, targetId)
                    setEmojiNotification(targetId)
                } else {
                    setNotification(type, title, body, targetId)
                }
            }
        }
    }

    private fun saveTokenToLocalStorage(token: String) {
        val prefs = applicationContext.getSharedPreferences("FCM_PREFS", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()

        // pending 상태 저장
        prefs.edit().putBoolean("token_needs_sync", true).apply()
    }

    private fun setNotification(type: String?, title: String?, body: String?, targetId: String?) {
        val intent: Intent = when (type) {
            "MOOD_CHECK" -> {
                Log.d("emoji notification2", "targetId: ${targetId}")
                Intent(this, NotificationActionReceiver::class.java).apply {
                    putExtra("diaryId", targetId)
                }
            }
            else -> {
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            }
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
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

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

    private fun setEmojiNotification(targetId: String?) {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val remoteViews = RemoteViews(packageName, R.layout.custom_notification)
        //remoteViews.setTextViewText(R.id.locationText, "${initialPlaceName}에서의 기분은 어떤가요?")
        remoteViews.setTextViewText(
            R.id.happyEmojiTextView,
            potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY.getEmotionUnicode()
        )
        remoteViews.setTextViewText(
            R.id.sadEmojiTextView,
            potatocake.katecam.everymoment.data.model.entity.Emotions.SAD.getEmotionUnicode()
        )
        remoteViews.setTextViewText(
            R.id.insensitiveEmojiTextView,
            potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE.getEmotionUnicode()
        )
        remoteViews.setTextViewText(
            R.id.angryEmojiTextView,
            potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY.getEmotionUnicode()
        )
        remoteViews.setTextViewText(
            R.id.confoundedEmojiTextView,
            potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED.getEmotionUnicode()
        )

        val emotions = listOf(
            R.id.happyEmojiTextView to potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY,
            R.id.sadEmojiTextView to potatocake.katecam.everymoment.data.model.entity.Emotions.SAD,
            R.id.insensitiveEmojiTextView to potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE,
            R.id.angryEmojiTextView to potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY,
            R.id.confoundedEmojiTextView to potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED
        )

        emotions.forEach { (viewId, emotion) ->
            val emotionIntent = Intent(this, NotificationActionReceiver::class.java).apply {
                action = "${emotion.name}_ACTION"
                putExtra("diaryId", targetId)
            }
            val emotionPendingIntent = PendingIntent.getBroadcast(
                this,
                viewId,
                emotionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            remoteViews.setOnClickPendingIntent(viewId, emotionPendingIntent)

            createEmojiNotificationChannel()
        }

        val builder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("지금의 기분은 어떠신가요?")
            .setContentIntent(pendingIntent)
            //.setContentText("현재 XX 위치에 머무르고 있어요! ")
            .setCustomContentView(remoteViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(EMOJI_NOTIFICATION_ID, builder.build())
    }

    private fun createEmojiNotificationChannel() {
        val descriptionText = getString(R.string.fcm_channel_description)
        val channel = NotificationChannel(
            EMOJI_CHANNEL_ID,
            EMOJI_CHANNEL_NAME,
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

        private const val EMOJI_NOTIFICATION_ID = 222222
        private const val EMOJI_CHANNEL_ID = "emoji_notification_channel"
        private const val EMOJI_CHANNEL_NAME = "emoji channelName"
    }
}