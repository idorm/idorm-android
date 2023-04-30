package org.appcenter.inudorm.presentation.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWithdrawalBinding
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.usecase.Withdraw
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.OkDialog

class WithdrawalActivity : AppCompatActivity() {
    fun withdraw() {
        lifecycleScope.launch {
            kotlin.runCatching {
                Withdraw(prefsRepository).run(null)
            }.onSuccess {
                OkDialog(
                    getString(R.string.withdrawSuccess),
                    "탈퇴 완료",
                    onOk = { goLogin() }).show(this@WithdrawalActivity)

            }.onFailure {
                OkDialog(
                    getString(R.string.withdrawFailure),
                    "탈퇴 실패",
                    onOk = { goLogin() }).show(this@WithdrawalActivity)

            }
        }
    }

    private fun goLogin() {
        lifecycleScope.launch {
            prefsRepository.signOut()
        }
        finish() // withdrawal
        finish() // main
        startActivity(Intent(this@WithdrawalActivity, LoginActivity::class.java))
    }

    fun back() {
        onBackPressed()
    }

    private val binding: ActivityWithdrawalBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_withdrawal)
    }

    private val prefsRepository: PrefsRepository by lazy {
        PrefsRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

    }
}