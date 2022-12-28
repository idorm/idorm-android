package org.appcenter.inudorm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 불호 요소들의 경우 Serialize/Deserialize 과정에서 Boolean 값을 뒤집어줘야 합니다.
 * */
@Parcelize
data class RoomMateFilter(
    var dormNum: Dorm,
    var isAllowedFood: Boolean = true,
    var isGrinding: Boolean = true,
    var isSmoking: Boolean = true,
    var isSnoring: Boolean = true,
    var isWearEarphones: Boolean = false,
    var joinPeriod: JoinPeriod,
    var maxAge: Int,
    var minAge: Int,
    var disAllowedFeatures: MutableList<Int>,
) : Parcelable