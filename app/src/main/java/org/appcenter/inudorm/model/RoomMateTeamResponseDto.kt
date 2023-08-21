package org.appcenter.inudorm.model

data class RoomMateTeamResponseDto(
    val isNeedToConfirmDeleted: Boolean,
    val members: List<TeamProfile>,
    val teamId: Int,
)