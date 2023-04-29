package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.model.ReportRequestDto
import org.appcenter.inudorm.presentation.board.State

class Report : ResultUseCase<ReportRequestDto, Boolean>() {
    override suspend fun onExecute(params: ReportRequestDto): Boolean {
        userRepository.report(params)
        return true
    }
}