package org.appcenter.inudorm.presentation.matching

import org.appcenter.inudorm.model.board.Photo

sealed class MyInfoMutationEvent(override val mutation: Mutation<*, *>) : MutationEvent(mutation) {
    class UpdateProfilePhoto(mutation: Mutation<Photo?, Unit>) : MyInfoMutationEvent(mutation)
    class DeleteProfilePhoto(mutation: Mutation<Nothing?, Unit>) : MyInfoMutationEvent(mutation)
}