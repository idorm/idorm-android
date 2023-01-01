package org.appcenter.inudorm.presentation.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.material.R.id.snackbar_text
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.appcenter.inudorm.OnSnackBarCallListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMainBinding
import org.appcenter.inudorm.presentation.HomeFragment
import org.appcenter.inudorm.presentation.MainViewModel
import org.appcenter.inudorm.presentation.mypage.MyPageFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, OnSnackBarCallListener {
    private val TAG = "[MainActivity]"

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment()).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment()).commit()
            }
            R.id.matching -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MatchingFragment()).commit()
            }
//            R.id.community -> {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, BoardFragment()).commit()
//            }
//            R.id.calendar -> {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, CalendarFragment()).commit()
//            }
            R.id.mypage -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MyPageFragment()).commit()
            }
        }
        return true
    }

    override fun onSnackBarCalled(message:String, duration:Int) {
        Snackbar.make(binding.container, message, duration)
            .apply {
                anchorView = binding.bottomNavigation
                view.findViewById<TextView>(snackbar_text).textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            .show()
    }

}