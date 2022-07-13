package org.appcenter.inudorm.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityRegisterBinding
import org.appcenter.inudorm.presentation.register.EmailPromptFragment
import org.appcenter.inudorm.util.PagerAdapter

class RegisterActivity : FragmentActivity(), OnPromptDoneListener {

    private val binding: ActivityRegisterBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_register)
    }

    private var registerBundle:Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
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

    override fun onPromptDone(data: Bundle) {
        registerBundle.putAll(data)
        Toast.makeText(this, registerBundle.getString("email"), Toast.LENGTH_SHORT).show()
        binding.pager.currentItem++
    }

}