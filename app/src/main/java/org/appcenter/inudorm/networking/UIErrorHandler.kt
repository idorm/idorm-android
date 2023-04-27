package org.appcenter.inudorm.networking

import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.http2.StreamResetException
import org.appcenter.inudorm.OnSnackBarCallListener
import org.appcenter.inudorm.R
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.IDormLogger
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

object UIErrorHandler {
    /**
     * @param context OnSnackBarCallListener 를 상속 받은 Activity 밑에서만 사용하세요!!
     * @param handleIDormError 여기에서 IDormError 를 처리하세요!
     */
    fun handle(
        context: Context,
        prefsRepository: PrefsRepository,
        error: Throwable,
        handleNetworkError: ((Throwable) -> Unit)? = null,
        handleIDormError: ((IDormError) -> Unit)? = null,
    ) {
        fun openSnackBar(message: String) = (context as OnSnackBarCallListener).openSnackBar(
            message,
            Snackbar.LENGTH_LONG
        )
        error.printStackTrace()
        when (error) {
            is SocketTimeoutException,
            is UnknownHostException,
            is SSLHandshakeException,
            is StreamResetException,
            is ConnectException,
            -> {
                if (handleNetworkError == null)
                    openSnackBar(context.getString(R.string.noNetworkConnection))
                else handleNetworkError(error)
            }
            is IDormError -> {
                // 로그인 안된 경우 처리
                if (error.error == ErrorCode.UNAUTHORIZED_MEMBER) {
                    CoroutineScope(Dispatchers.IO).launch {
                        prefsRepository.signOut()
                    }
                }
                if (handleIDormError == null)
                    openSnackBar(error.message)
                else handleIDormError(error)
            }
            else -> {
                IDormLogger.e(this, error.toString())
                Toast.makeText(context, error.message ?: "알 수 없는 오류입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}