package org.appcenter.inudorm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.appcenter.inudorm.presentation.MainActivity
import org.appcenter.inudorm.presentation.SplashActivity
import org.appcenter.inudorm.presentation.board.BoardFragment
import org.appcenter.inudorm.presentation.board.PostDetailActivity
import org.appcenter.inudorm.service.FCMService.NotificationChannel
import org.appcenter.inudorm.util.IDormLogger

class NotificationHubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            val channelId = bundle.getString("channelId")
            val channel = NotificationChannel.fromId(channelId ?: "NONE")
            val mainIntent = Intent(this, MainActivity::class.java)
            val contentId = bundle.getString("contentId")?.toIntOrNull()
            IDormLogger.d(this, contentId.toString())
            when (channel) {
                NotificationChannel.Comment,
                NotificationChannel.SubComment,
                NotificationChannel.TopPost,
                NotificationChannel.Announcement,
                -> {
                    startActivity(mainIntent)
                    val intent = Intent(this, PostDetailActivity::class.java)
                    intent.putExtra("id", contentId)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
            }
            for (key in bundle.keySet()) {
                IDormLogger.d(
                    this,
                    key + " : " + if (bundle.get(key) != null) bundle.get(key) else "NULL"
                )
            }
        }
    }
}