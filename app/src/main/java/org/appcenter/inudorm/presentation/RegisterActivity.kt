package org.appcenter.inudorm.presentation

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityRegisterBinding
import org.appcenter.inudorm.presentation.account.CodePromptFragment
import org.appcenter.inudorm.presentation.account.EmailPromptFragment
import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.presentation.account.PasswordPromptFragment
import org.appcenter.inudorm.util.PagerAdapter

class RegisterActivity : AppCompatActivity(), OnPromptDoneListener {

    private val binding: ActivityRegisterBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_register)
    }

    private var registerBundle: Bundle = Bundle()
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setUpActionBar()
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            setUpViewPager()
        }
    }

    override fun onPromptDone(data: Bundle) { // Fragment에서 보내주신 소중한 이벤트를 갖고 와요..
        registerBundle.putAll(data)
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
                    val bundle = Bundle()
                    bundle.putSerializable("purpose", EmailPromptPurpose.FindPass)
                    val fragment = PasswordPromptFragment()
                    fragment.arguments = data
                    addPage(fragment)
                }
            }
        binding.pager.currentItem++
        setToolbarIcon()
    }


    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
    }

    private fun setUpViewPager() {
        val initPage = ArrayList<Fragment>()
        initPage.add(EmailPromptFragment())
        binding.pager.isUserInputEnabled = false
        pagerAdapter = PagerAdapter(this, initPage)
        binding.pager.adapter = pagerAdapter
    }


    private fun addPage(page: Fragment) {
        pagerAdapter.addFragment(page)
    }

    override fun onBackPressed() { // 스텝이 첫번째일 경우 원래 백버튼 이벤트 리스너 호출
        if (binding.pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.pager.currentItem--
            pagerAdapter.deleteFragment(binding.pager.currentItem + 1)
            setToolbarIcon()
        }
    }

    private fun setToolbarIcon() {
        val icon =
            if (binding.pager.currentItem == 1) R.drawable.ic_baseline_clear_24
            else R.drawable.ic_baseline_arrow_back_24
        supportActionBar!!.setHomeAsUpIndicator(icon)
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