package org.appcenter.inudorm.model

data class Calendar(
    val calendarId: Long,
    val isDorm1Yn: Boolean,
    val isDorm2Yn: Boolean,
    val isDorm3Yn: Boolean,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val content: String,
    val location: String,
    val url: String,
)