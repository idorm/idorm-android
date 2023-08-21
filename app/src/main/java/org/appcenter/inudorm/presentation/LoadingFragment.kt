package org.appcenter.inudorm.presentation

import android.app.Dialog
import android.os.Bundle
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.util.State

open class LoadingFragment : Fragment() {

    fun setLoadingState(loading: Boolean) {
        (requireActivity() as LoadingActivity).setLoadingState(loading)
    }

    fun setLoadingState(state: State<*>) {
        (requireActivity() as LoadingActivity).setLoadingState(
            state.isLoading()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 모든 LoadingFragment 는 최초에 로딩을 해제.
        setLoadingState(false)
    }
}