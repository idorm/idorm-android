package org.appcenter.inudorm.model


enum class Gender {
    Male,
    Female
}

enum class JoinPeriod {
    Long,
    Short
}

/**
 * 매칭을 위한 정보에 대한 데이터입니다.
 * dormNum: 기숙사 식별자
 * joinPeriod: 입사 기간 (16/24주 / 8/12주*부정확)
 * gender: Gender enum type
 * snoring: 코골이 여부
 * grinding: 이갈이 여부
 * member: 유저 식별자
 */
data class MyInfo(
    val id: Long,
    val member: Long,
    val dormNum: Long,
    val joinPeriod: JoinPeriod,
    val gender: Gender,
    val age: Int,
    val snoring: Boolean,
    val smoking: Boolean,
    val grinding: Boolean,
    val wearEarphones: Boolean,
    val allowedFood: Boolean,
    val wakeUpTime: String,
    val cleanUpStatus: String,
    val showerTime: String,
    val mbti: String?,
    val wishText: String?,
    val chatLink: String?,
)