package org.appcenter.inudorm.model

data class TeamSchedule(
    override val calendarId: Long,
    override val startDate: String?,
    override val endDate: String?,
    override val startTime: String?,
    override val endTime: String?,
    override val content: String?,
    override val url: String?,
    val targets: List<TeamProfile>,
    val isSleepover: Boolean,
    val title: String
) : ScheduleData(
    calendarId,
    startDate,
    endDate,
    startTime,
    endTime,
    content,
    url
)