package org.appcenter.inudorm.model

data class EmailVerifyResponseDto(val email: String, val check: Boolean, val join: Boolean)