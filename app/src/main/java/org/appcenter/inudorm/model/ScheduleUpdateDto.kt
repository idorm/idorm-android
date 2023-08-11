package org.appcenter.inudorm.model

data class ScheduleUpdateDto(
    val calendarId: Long?,
    val startDate: String?,
    val endDate: String?,
    val startTime: String?,
    val endTime: String?,
    val content: String?,
    val url: String?,
    val targets: ArrayList<TeamProfile>,
    val title: String,
) : ReqBody()