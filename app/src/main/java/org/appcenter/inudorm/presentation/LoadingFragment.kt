package org.appcenter.inudorm.presentation

import android.app.Dialog
import android.os.Bundle
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R

open class LoadingFragment : Fragment() {

    fun setLoadingState(loading: Boolean) {
        (requireActivity() as LoadingActivity).setLoadingState(loading)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}