package org.appcenter.inudorm.presentation.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWithdrawalBinding
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.usecase.Withdraw
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton

class WithdrawalActivity : AppCompatActivity() {
    fun withdraw() {
        lifecycleScope.launch {
            kotlin.runCatching {
                Withdraw().run(null)
            }.onSuccess {
                CustomDialog(
                    "탈퇴 처리가 성공적으로 완료되었습니다.\n" +
                            "언제든지 다시 돌아와 주세요!",
                    "탈퇴 완료",
                    positiveButton = signOutButton
                )

            }.onFailure {
                CustomDialog(
                    "회원탈퇴에 실패했습니다. 잠시 후 다시 시도해주세요.",
                    positiveButton = signOutButton
                )
            }
        }
    }

    private val signOutButton = DialogButton("확인", onClick = {
        signOutAndGoLogin()
    })

    private fun signOutAndGoLogin() {
        lifecycleScope.launch {
            prefsRepository.signOut()
        }
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