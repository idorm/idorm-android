package org.appcenter.inudorm.model

data class SleepOverUpdateDto(
    val startDate: String,
    val endDate: String,
    val teamCalendarId: Long
)