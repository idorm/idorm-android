package org.appcenter.inudorm.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.MainActivity
import org.appcenter.inudorm.util.IDormLogger

class FCMService : FirebaseMessagingService() {

    enum class NotificationChannel(val id: String, val displayName: String, val desc: String) {
        Calendar("calendar", "캘린더", "캘린더 알림을 활성화합니다."),
        Comment("comment", "댓글", "댓글 알림을 활성화합니다."),
        SubComment("subComment", "대댓글", "대댓글 알림을 활성화합니다."),
        Announcement("announcement", "공지", "공지 알림을 활성화합니다.");

        companion object {
            private val map = NotificationChannel.values().associateBy { it.id }
            infix fun fromId(value: String) = map[value]
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        IDormLogger.d(this, "Refreshed token: $token")

        // Todo: Update FCM Token
        CoroutineScope(Dispatchers.IO).launch {
            App.userRepository
        }
    }

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(
        channel: String,
        channel_nm: String,
        desc: String,
    ) {

        val channelMessage = NotificationChannel(
            channel, channel_nm,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channelMessage.description = desc
        channelMessage.enableLights(true)
        channelMessage.enableVibration(true)
        channelMessage.setShowBadge(false)
        channelMessage.vibrationPattern = longArrayOf(1000, 1000)
        notificationManager.createNotificationChannel(channelMessage)
    }

    private fun createNotification(
        channel: NotificationChannel,
        title: String,
        message: String,
        pendingIntent: PendingIntent,
        @DrawableRes
        icon: Int = R.mipmap.ic_launcher,
    ) {
        val notification = NotificationCompat.Builder(this, channel.id)
            .setSmallIcon(icon)
            .setContentTitle(title) //푸시알림의 제목
            .setContentText(message) //푸시알림의 내용
            .setChannelId(channel.id)
            .setAutoCancel(true) //선택시 자동으로 삭제되도록 설정.
            .setContentIntent(pendingIntent) //알림을 눌렀을때 실행할 인텐트 설정.
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        notificationManager.notify(9999, notification)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        IDormLogger.d(this, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            // data, 거의 쓸일 없음
            IDormLogger.d(this, "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            IDormLogger.d(
                this,
                "Message Notification \nTitle: ${it.title}\nBody: ${it.body}\nChannel Id: ${it.channelId}"
            )
            // 안드로이드 버전이 알림 채널을 지원하는지? (오레오 이상)
            val notificationChannel = NotificationChannel.fromId(it.channelId ?: "") ?: return
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                notificationManager.getNotificationChannel(it.channelId ?: "") == null
            ) {
                createChannel(
                    it.channelId ?: "",
                    notificationChannel.displayName,
                    notificationChannel.desc,
                )
            }
            createNotification(
                notificationChannel,
                it.title ?: "",
                it.body ?: "",
                PendingIntent.getActivity(
                    applicationContext,
                    33,
                    Intent(this@FCMService, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                ),
            )
        }

    }

}
