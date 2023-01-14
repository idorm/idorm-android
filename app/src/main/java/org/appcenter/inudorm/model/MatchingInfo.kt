package org.appcenter.inudorm.model


/**
 * 매칭을 위한 정보에 대한 데이터입니다.
 * dormNum: 기숙사 식별자
 * joinPeriod: 입사 기간 (16/24주 / 8/12주*부정확)
 * gender: Gender enum type
 * snoring: 코골이 여부
 * grinding: 이갈이 여부
 * member: 유저 식별자
 */
data class MatchingInfo(
    val addedAt: String? = null,
    val memberId: Int,
    val matchingInfoId: Int,
    val dormNum: Dorm,
    val joinPeriod: JoinPeriod,
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