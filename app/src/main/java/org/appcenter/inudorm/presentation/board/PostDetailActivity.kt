package org.appcenter.inudorm.presentation.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.appcenter.inudorm.R

/**
 * 커뮤니티 글 보기 페이지에서 눌러서 들어가는 페이지. PostList
 */
class PostDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
    }
}