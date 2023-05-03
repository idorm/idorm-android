package org.appcenter.inudorm.presentation.account

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityOnboardBinding
import org.appcenter.inudorm.presentation.PromptActivity
import org.appcenter.inudorm.presentation.onboard.AdditionalInformationFragment
import org.appcenter.inudorm.presentation.onboard.BaseInfoPurpose
import org.appcenter.inudorm.presentation.onboard.BaseInformationFragment
import org.appcenter.inudorm.util.IDormLogger

class OnboardActivity : PromptActivity() {
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
        val onboardIntent = intent
        val bundle = Bundle()

        bundle.putSerializable("purpose", onboardIntent.getSerializableExtra("purpose"))
        baseFragment.arguments = bundle

        initPage.add(baseFragment)
        initPage.add(additionalFragment)
        return initPage
    }

    override fun onPromptDone(data: Bundle) {
        // Todo: 한 페이지에서 입력 끝났을때
    }

}