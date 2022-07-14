package org.appcenter.inudorm.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import org.appcenter.inudorm.OnPromptDoneListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.account.EmailPromptFragment
import org.appcenter.inudorm.util.PagerAdapter

abstract class PromptActivity : AppCompatActivity(), OnPromptDoneListener {

    lateinit var pagerAdapter: PagerAdapter
    private lateinit var pager: ViewPager2

    fun setUpActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
    }

    fun setUpViewPager(pagerToSetup: ViewPager2) {
        pager = pagerToSetup
        val initPage = ArrayList<Fragment>()
        initPage.add(EmailPromptFragment())
        pager.isUserInputEnabled = false
        pagerAdapter = PagerAdapter(this, initPage)
        pager.adapter = pagerAdapter
    }

    fun addPage(page: Fragment) {
        pagerAdapter.addFragment(page)
    }

    override fun onBackPressed() { // 스텝이 첫번째일 경우 원래 백버튼 이벤트 리스너 호출
        if (pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            pager.currentItem--
            pagerAdapter.deleteFragment(pager.currentItem + 1)
            setToolbarIcon()
        }
    }

    fun setToolbarIcon() {
        val icon =
            if (pager.currentItem == 1) R.drawable.ic_baseline_clear_24
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