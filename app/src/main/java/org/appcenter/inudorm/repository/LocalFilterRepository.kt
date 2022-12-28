package org.appcenter.inudorm.repository

import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.RoomMateFilter

/**
 * Filter, Matching에서 사용하는 Local Filter를 관리하는 Repository 입니다. :)
 */
class LocalFilterRepository {
    var roomMateFilter: RoomMateFilter = RoomMateFilter(
        dormNum = Dorm.DORM1,
        joinPeriod = JoinPeriod.WEEK16,
        minAge = 20,
        maxAge = 40,
        disAllowedFeatures = emptyList()
    )
}