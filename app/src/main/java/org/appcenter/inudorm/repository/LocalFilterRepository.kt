package org.appcenter.inudorm.repository

import org.appcenter.inudorm.model.JoinPeriod

enum class Dorm(val code: String) {
    First("DORM1"),
    Second("DORM2"),
    Third("DORM3"),
}

data class RoomMateFilter(
    var dormNum: Dorm,
    var isAllowedFood: Boolean? = null,
    var isGrinding: Boolean? = null,
    var isSmoking: Boolean? = null,
    var isSnoring: Boolean? = null,
    var isWearEarphones: Boolean? = null,
    var joinPeriod: JoinPeriod,
    var maxAge: Int,
    var minAge: Int,
)

class LocalFilterRepository {
    var _roomMateFilter: RoomMateFilter? = null
}