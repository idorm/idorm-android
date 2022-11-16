package org.appcenter.inudorm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
    var disAllowedFeatures: List<Int>,
) : Parcelable