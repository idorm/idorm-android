package org.appcenter.inudorm

import android.app.Dialog
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

open class LoadingActivity : AppCompatActivity() {
    private val dialog: Dialog by lazy {
        Dialog(this, R.style.NewDialog)
    }

    fun setLoadingState(loading: Boolean) {
        if (loading) {
            dialog.setContentView(ProgressBar(this).apply {
                indeterminateTintList =
                    ContextCompat.getColorStateList(this@LoadingActivity, R.color.iDorm_blue)
            }) // ProgressBar 위젯 생성
            dialog.setCanceledOnTouchOutside(false) // 외부 터치 막음
            dialog.setOnCancelListener { this.finish() } // 뒤로가기시 현재 액티비티 종료
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }
}