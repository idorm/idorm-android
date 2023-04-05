package org.appcenter.inudorm.presentation.onboard

import org.appcenter.inudorm.util.ViewModelWithEvent



sealed class OnboardEvent(open val value: String)

class BaseInformationViewModel : ViewModelWithEvent() {


}