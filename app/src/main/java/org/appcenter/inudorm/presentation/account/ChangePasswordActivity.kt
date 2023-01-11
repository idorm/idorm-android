package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityFindPassBinding
import org.appcenter.inudorm.presentation.PromptActivity
import org.appcenter.inudorm.presentation.account.prompt.PasswordPromptFragment

/**
 * 비밀번호 찾기를 재활용해 비밀번호 입력 프롬포트만 띄웁니당
 */
class ChangePasswordActivity : PromptActivity() {

    private val binding: ActivityFindPassBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_find_pass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        binding.toolbarText.text = "비밀번호 변경"
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            setUpViewPager(binding.pager, getInitPages())
        }
    }

    private fun getInitPages(): ArrayList<Fragment> {
        return arrayListOf(PasswordPromptFragment())
    }


    override fun onPromptDone(data: Bundle) { // Fragment에서 보내주신 소중한 이벤트를 갖고 와요..
        // Todo: 네트워크 요청 날려서 반환받기.

    }
}