package org.appcenter.inudorm.presentation

import android.app.Dialog
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.appcenter.inudorm.R

open class LoadingFragment : Fragment() {

    fun setLoadingState(loading: Boolean) {
        (this@LoadingFragment.requireActivity() as MainActivity).setLoadingState(loading)
    }

}