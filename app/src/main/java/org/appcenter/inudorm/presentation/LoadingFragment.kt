package org.appcenter.inudorm.presentation

import android.app.Dialog
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.R

open class LoadingFragment : Fragment() {
    private val dialog: Dialog by lazy {
        Dialog(this@LoadingFragment.requireContext(), R.style.NewDialog)
    }

    fun setLoadingState(loading: Boolean) {
        if (loading) {
            dialog.setContentView(ProgressBar(this@LoadingFragment.requireContext()).apply {
                indeterminateTintList =
                    ContextCompat.getColorStateList(
                        this@LoadingFragment.requireContext(),
                        R.color.iDorm_blue
                    )
            }) // ProgressBar 위젯 생성
            dialog.setCanceledOnTouchOutside(false) // 외부 터치 막음
            dialog.setOnCancelListener {
                this@LoadingFragment.requireActivity().finish()
            } // 뒤로가기시 현재 액티비티 종료
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

}