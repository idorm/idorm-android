package org.appcenter.inudorm.repository

import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.presentation.defaultFilter

/**
 * Filter, Matching에서 사용하는 Local Filter를 관리하는 Repository 입니다. :)
 */
class LocalFilterRepository {
    var roomMateFilter: RoomMateFilter = defaultFilter
}