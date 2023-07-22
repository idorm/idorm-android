package org.appcenter.inudorm.model

open class CalendarData(
    open val calendarId: Long,
    open val startDate: String?,
    open val endDate: String?,
    open val startTime: String?,
    open val endTime: String?,
    open val content: String?,
    open val url: String?,
)

data class Calendar(
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
) : CalendarData(
    calendarId,
    startDate,
    endDate,
    startTime,
    endTime,
    content,
    url

)