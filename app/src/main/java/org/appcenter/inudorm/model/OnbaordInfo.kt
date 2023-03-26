package org.appcenter.inudorm.model

data class OnbaordInfo (
    var memberId: Int,
    var dormNum: Dorm,
    var joinPeriod: JoinPeriod,
    val gender: Gender,
    val age: Int,
    val isSnoring: Boolean,
    val isGrinding: Boolean,
    val isSmoking: Boolean,
    val isAllowedFood: Boolean,
    val isWearEarphones: Boolean,
    val isMatchingInfoPublic: Boolean?,
    val wakeUpTime: String,
    val cleanUpStatus: String,
    val showerTime: String,
    val openKakaoLink: String,
    val mbti: String,
    val wishText: String,
)