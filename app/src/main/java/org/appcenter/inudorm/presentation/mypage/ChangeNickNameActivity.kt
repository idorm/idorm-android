package org.appcenter.inudorm.presentation.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityChangeNickNameBinding
import org.appcenter.inudorm.databinding.ActivityFindPassBinding
import org.appcenter.inudorm.presentation.PromptActivity
import org.appcenter.inudorm.presentation.account.prompt.NickNamePromptFragment
import org.appcenter.inudorm.presentation.account.prompt.PasswordPromptFragment

class ChangeNickNameActivity : PromptActivity() {

    private val binding: ActivityChangeNickNameBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_change_nick_name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            setUpViewPager(binding.pager, getInitPages())
        }
    }

    private fun getInitPages(): ArrayList<Fragment> {
        return arrayListOf(NickNamePromptFragment())
    }


    override fun onPromptDone(data: Bundle) { // Fragment에서 보내주신 소중한 이벤트를 갖고 와요..
        // Todo: 네트워크 요청 날려서 반환받기.

    }

}