package org.appcenter.inudorm.presentation.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWelcomeBinding
import org.appcenter.inudorm.presentation.MainActivity

class WelcomeActivity : AppCompatActivity() {

    private val binding : ActivityWelcomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_welcome)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.button3.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}