package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.matchingInfoRepository
import org.appcenter.inudorm.model.OnboardInfo
import org.appcenter.inudorm.presentation.onboard.BaseInfoPurpose
import kotlin.reflect.KSuspendFunction1


data class OnboardParams(val codeType: BaseInfoPurpose, val onboardInfo: OnboardInfo)

private val sendRepos : Map<BaseInfoPurpose, KSuspendFunction1<OnboardParams, OnboardInfo>> = mapOf(
    BaseInfoPurpose.Create to matchingInfoRepository::saveMatchingInfo,
    BaseInfoPurpose.Edit to matchingInfoRepository::editMatchingInfo
)

class UserOnboard : ResultUseCase<OnboardParams, OnboardInfo> () {

    override suspend fun onExecute(params: OnboardParams): OnboardInfo {
        return sendRepos[params.codeType]?.invoke(params)!!
    }
}