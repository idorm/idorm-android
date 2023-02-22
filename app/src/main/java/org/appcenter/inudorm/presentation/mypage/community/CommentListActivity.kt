package org.appcenter.inudorm.presentation.mypage.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.appcenter.inudorm.R

/**
 * 내가 쓴 댓글 등 댓글 리스트를 보여주는 액티비티
 */
class CommentListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)
    }
}