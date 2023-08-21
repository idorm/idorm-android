package org.appcenter.inudorm.model

data class TeamScheduleReq (
    val content: String?,
    val startDate: String?,
    val endDate: String?,
    val startTime: String?,
    val endTime: String?,
    val targets: List<TeamProfile>,
    val title: String
)