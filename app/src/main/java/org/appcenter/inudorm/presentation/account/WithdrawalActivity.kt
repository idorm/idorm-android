package org.appcenter.inudorm.presentation.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWithdrawalBinding

class WithdrawalActivity : AppCompatActivity() {
    fun withdraw() {

    }

    fun back() {
        onBackPressed()
    }

    private val binding: ActivityWithdrawalBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_withdrawal)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

    }
}