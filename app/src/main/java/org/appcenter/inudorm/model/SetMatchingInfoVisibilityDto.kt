package org.appcenter.inudorm.model

import org.appcenter.inudorm.networking.service.ReqBody
import org.appcenter.inudorm.usecase.SetMatchingInfoVisibility

data class SetMatchingInfoVisibilityDto(val isMatchingInfoPublic: Boolean) : ReqBody()