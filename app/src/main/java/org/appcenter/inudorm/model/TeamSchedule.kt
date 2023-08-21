package org.appcenter.inudorm.model

data class TeamSchedule(
    val teamCalendarId: Long,
    override val startDate: String?,
    override val endDate: String?,
    override val startTime: String?,
    override val endTime: String?,
    override val content: String?,
    override val url: String?,
    val targets: List<TeamProfile>,
    val isSleepover: Boolean,
    val title: String,
) : ScheduleData(
    teamCalendarId,
    startDate,
    endDate,
    startTime,
    endTime,
    content,
    url
)