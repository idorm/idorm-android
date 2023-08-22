package org.appcenter.inudorm.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.R.id.snackbar_text
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.OnSnackBarCallListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMainBinding
import org.appcenter.inudorm.presentation.board.BoardFragment
import org.appcenter.inudorm.presentation.calendar.CalendarFragment
import org.appcenter.inudorm.presentation.matching.MatchingFragment
import org.appcenter.inudorm.presentation.mypage.MyPageFragment
import org.appcenter.inudorm.util.IDormLogger
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaClass

const val UPDATE_REQ_CODE = 34843

class MainActivity : BottomNavigationView.OnNavigationItemSelectedListener, OnSnackBarCallListener,
    LoadingActivity() {
    private val TAG = "[MainActivity]"


    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = mainViewModel
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)

        val dest = intent.getStringExtra("dest")
        var fragment: Fragment = HomeFragment()
        if (dest == "CalendarFragment") {
            fragment = CalendarFragment()
            val bundle = Bundle()
            bundle.putInt("inviter", intent.getIntExtra("inviter", -999))
            IDormLogger.i(this, "inviter from intent: ${intent.getIntExtra("inviter", -999)}")
            fragment.arguments = bundle
            binding.bottomNavigation.selectedItemId = R.id.calendar
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottomHome -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment()).commit()
            }

            R.id.matching -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MatchingFragment()).commit()
            }

            R.id.community -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, BoardFragment()).commit()
            }

            R.id.calendar -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CalendarFragment()).commit()
            }

            R.id.mypage -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MyPageFragment()).commit()
            }
        }
        return true
    }

    override fun onSnackBarCalled(message: String, duration: Int) {
        Snackbar.make(binding.container, message, duration)
            .apply {
                anchorView = binding.bottomNavigation
                view.findViewById<TextView>(snackbar_text).textAlignment =
                    View.TEXT_ALIGNMENT_CENTER
            }
            .show()
    }

}