package org.appcenter.inudorm.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityRegisterBinding
import org.appcenter.inudorm.presentation.register.EmailPromptFragment
import org.appcenter.inudorm.util.PagerAdapter

class RegisterActivity : AppCompatActivity(), OnPromptDoneListener {

    private val binding: ActivityRegisterBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_register)
    }

    private var registerBundle:Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            val initPages = ArrayList<Fragment>()
            initPages.add(EmailPromptFragment())
            val pagerAdapter = PagerAdapter(this, initPages)
            binding.pager.adapter = pagerAdapter
        }
    }
    override fun onBackPressed() { // 스텝이 첫번째일 경우 원래 백버튼 이벤트 리스너 호출
        if (binding.pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.pager.currentItem--
        }
    }
    // 4.툴바 메뉴 버튼이 클릭 됐을 때 콜백
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when(item.itemId){
            android.R.id.home->{ // 메뉴 버튼
               this.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPromptDone(data: Bundle) {
        registerBundle.putAll(data)
        Toast.makeText(this, registerBundle.getString("email"), Toast.LENGTH_SHORT).show()
        binding.pager.currentItem++
    }

}