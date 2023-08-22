package org.appcenter.inudorm.presentation.matching

import org.appcenter.inudorm.model.board.Photo
import org.appcenter.inudorm.presentation.calendar.TeamMutation

sealed class RoomMutationEvent(override val mutation: Mutation<*, *>) : MutationEvent(mutation) {

    class DeleteMate(mutation: Mutation<Int, Unit>) : RoomMutationEvent(mutation)
    class DeleteSchedule(mutation: Mutation<Int, Unit>) : RoomMutationEvent(mutation)
    class Leave(mutation: Mutation<Nothing?, Unit>) : RoomMutationEvent(mutation)
}