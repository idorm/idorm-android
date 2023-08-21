package org.appcenter.inudorm.model

data class TeamProfile(
    val nickname: String?,
    val memberId: Int? = null,
    val order: Int? = null,
    val profilePhotoUrl: String? = null,
    val hasInvitedToSchedule: Boolean? = null,
)