package org.appcenter.inudorm.presentation.mypage.community

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityPostListBinding

/**
 * 내가 쓴 글, 공감한 글 등 특정 조건을 충족하는 글들을 최신/과거순으로 보여주는 액티비티
 */
abstract class PostListActivity : LoadingActivity() {
    val binding: ActivityPostListBinding by lazy {
        DataBindingUtil.setContentView(this@PostListActivity, R.layout.activity_post_list)
    }

    abstract val title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarText.text = title
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼, 사실상 백버튼으로 취급하면 됩니다!
                this.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}