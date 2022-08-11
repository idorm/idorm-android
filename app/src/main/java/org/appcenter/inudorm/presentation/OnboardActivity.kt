package org.appcenter.inudorm.presentation

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityOnboardBinding
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.presentation.onboard.AdditionalInformationFragment
import org.appcenter.inudorm.presentation.onboard.BaseInformationFragment
import java.util.*

class OnboardActivity : PromptActivity() {
    //TODO : 유즈케이스랑 리사이클러뷰 등록 좀 부탁합니당. 혹시 이거 할 때 되면 말해 줭! 나 구조 설명 좀 듣구 싶어성ㅎㅎㅎ
    private var onBoardBundle: Bundle = Bundle()

    private val binding: ActivityOnboardBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_onboard)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            setUpViewPager(binding.pager, getInitPages())
        }
    }

    private fun getInitPages() : ArrayList<Fragment> {
        val initPage = ArrayList<Fragment>()
        val baseFragment = BaseInformationFragment()
            val additionalFragment = AdditionalInformationFragment()
        initPage.add(baseFragment)
        initPage.add(additionalFragment)
        return initPage
    }

    override fun onPromptDone(data: Bundle) {
        // Todo: 한 페이지에서 입력 끝났을때
    }

}