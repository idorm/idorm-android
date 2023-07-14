package org.appcenter.inudorm.model

class TeamCalendar (
    //일단 복붙해온 것이에여. 다시 해줘야 함.
    val calendarId: Long,
    val isDorm1Yn: Boolean,
    val isDorm2Yn: Boolean,
    val isDorm3Yn: Boolean,
    val startDate: String?,
    val endDate: String?,
    val startTime: String?,
    val endTime: String?,
    val content: String?,
    val location: String?,
    val url: String?,
)