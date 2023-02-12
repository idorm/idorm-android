package org.appcenter.inudorm.presentation.board

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.appcenter.inudorm.R

/**
 * 내가 쓴 글, 공감한 글 등 특정 조건을 충족하는 글들을 최신/과거순으로 보여주는 액티비티
 */
class PostListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
    }
}