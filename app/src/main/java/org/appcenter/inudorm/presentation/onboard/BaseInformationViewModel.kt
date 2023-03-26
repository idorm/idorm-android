package org.appcenter.inudorm.presentation.onboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.appcenter.inudorm.util.ViewModelWithEvent



sealed class OnboardEvent(open val value: String)

class BaseInformationViewModel : ViewModelWithEvent() {




}