package org.appcenter.inudorm.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityFindPassBinding
import org.appcenter.inudorm.presentation.account.CodePromptFragment
import org.appcenter.inudorm.presentation.account.EmailPromptFragment
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.presentation.account.PasswordPromptFragment

class FindPassActivity : PromptActivity() {

    private val binding: ActivityFindPassBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_find_pass)
    }

    private var findPassBundle: Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            setUpViewPager(binding.pager, getInitPages())
        }
    }

    private fun getInitPages() : ArrayList<Fragment> {
        val initPage = ArrayList<Fragment>()
        val fragment = EmailPromptFragment()
        val bundle = Bundle()
        bundle.putSerializable("purpose", EmailPromptPurpose.FindPass)
        fragment.arguments = bundle
        initPage.add(fragment)
        return initPage
    }


    override fun onPromptDone(data: Bundle) { // Fragment에서 보내주신 소중한 이벤트를 갖고 와요..
        findPassBundle.putAll(data)
        if (binding.pager.currentItem == pagerAdapter.itemCount - 1)
            when (binding.pager.currentItem) {
                0 -> {
                    Toast.makeText(this, "암호화된 코드를 갖고 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    val fragment = CodePromptFragment()
                    fragment.arguments = data
                    addPage(fragment)
                }
                1 -> {
                    Toast.makeText(this, "비밀번호 설정 프래그먼트 생성", Toast.LENGTH_SHORT).show()
                    val fragment = PasswordPromptFragment()
                    fragment.arguments = data
                    addPage(fragment)
                }
            }
        binding.pager.currentItem++
        setToolbarIcon()
    }

}