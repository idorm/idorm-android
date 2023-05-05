package org.appcenter.inudorm.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.material.R.id.snackbar_text
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.OnSnackBarCallListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityMainBinding
import org.appcenter.inudorm.presentation.board.BoardFragment
import org.appcenter.inudorm.presentation.matching.MatchingFragment
import org.appcenter.inudorm.presentation.mypage.MyPageFragment
import org.appcenter.inudorm.util.OkDialog
import kotlin.system.exitProcess

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

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment()).commit()
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