package org.appcenter.inudorm.presentation.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityChangeNickNameBinding
import org.appcenter.inudorm.databinding.ActivityFindPassBinding
import org.appcenter.inudorm.model.ChangeNickNameDto
import org.appcenter.inudorm.presentation.PromptActivity
import org.appcenter.inudorm.presentation.account.LoginActivity
import org.appcenter.inudorm.presentation.account.prompt.NickNamePromptFragment
import org.appcenter.inudorm.presentation.account.prompt.PasswordPromptFragment
import org.appcenter.inudorm.usecase.ChangeNickName
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton

class ChangeNickNameActivity : PromptActivity() {

    private val binding: ActivityChangeNickNameBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_change_nick_name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            // The pager adapter, which provides the pages to the view pager widget.
            setUpViewPager(binding.pager, getInitPages())
        }
    }

    private fun getInitPages(): ArrayList<Fragment> {
        return arrayListOf(NickNamePromptFragment())
    }


    override fun onPromptDone(data: Bundle) { // Fragment에서 보내주신 소중한 이벤트를 갖고 와요..
        // Todo: 네트워크 요청 날려서 반환받기.
        lifecycleScope.launch {
            kotlin.runCatching {
                ChangeNickName().run(ChangeNickNameDto(data.getString("nickname")))
            }.onSuccess {
                CustomDialog(
                    "닉네임 변경이 완료되었습니다. 변경된 비밀번호로 다시 로그인해주세요.",
                    positiveButton = DialogButton("확인", onClick = {
                        onBackPressed()
                    })
                ).show(this@ChangeNickNameActivity)
            }.onFailure {
                CustomDialog(
                    "닉네임 변경에 실패했습니다. 잠시후 다시 시도해주세요.",
                    positiveButton = DialogButton("확인", onClick = {
                        onBackPressed()

                    })
                ).show(this@ChangeNickNameActivity)
            }
        }

    }

}
