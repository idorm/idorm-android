package org.appcenter.inudorm.model

data class OnboardInfo (
    val age: Int,
    val cleanUpStatus: String,
    var dormCategory: Dorm,
    val gender: Gender,
    val isAllowedFood: Boolean,
    val isGrinding: Boolean,
    val isSmoking: Boolean,
    val isSnoring: Boolean,
    val isWearEarphones: Boolean,
    var joinPeriod: JoinPeriod,
    val mbti: String,
    val openKakaoLink: String,
    val showerTime: String,
    val wakeupTime: String,
    val wishText: String,
)