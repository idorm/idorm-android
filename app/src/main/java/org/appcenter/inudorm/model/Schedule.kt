package org.appcenter.inudorm.model

abstract class ScheduleData(
    @Transient
    open val calendarId: Long,
    @Transient
    open val startDate: String?,
    @Transient
    open val endDate: String?,
    @Transient
    open val startTime: String?,
    @Transient
    open val endTime: String?,
    @Transient
    open val content: String?,
    @Transient
    open val url: String?,
)

data class Schedule(
    override val calendarId: Long,
    override val startDate: String?,
    override val endDate: String?,
    override val startTime: String?,
    override val endTime: String?,
    override val content: String?,
    override val url: String?,
    val isDorm1Yn: Boolean,
    val isDorm2Yn: Boolean,
    val isDorm3Yn: Boolean,
    val location: String?,
) : ScheduleData(
    calendarId,
    startDate,
    endDate,
    startTime,
    endTime,
    content,
    url

)