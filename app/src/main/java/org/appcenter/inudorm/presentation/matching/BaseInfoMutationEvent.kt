package org.appcenter.inudorm.presentation.matching

import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.usecase.OnboardParams

sealed class BaseInfoMutationEvent(override val mutation: Mutation<*, *>) : MutationEvent(mutation) {
    class CreateBaseInfo(override val mutation: Mutation<OnboardParams, OnboardInfo>) : BaseInfoMutationEvent(mutation)
    class EditBaseInfo(override val mutation: Mutation<OnboardParams, OnboardInfo>) : BaseInfoMutationEvent(mutation)
}