package org.appcenter.inudorm.util

import android.content.ActivityNotFoundException
import android.content.Context
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient

object KakaoShare {
    fun share(
        context: Context,
        templateId: Long,
        templateArgs: Map<String, String>?,
    ) {

        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)
        ) {
            // 카카오톡으로 카카오톡 공유 가능

            ShareClient.instance.shareCustom(
                context,
                templateId,
                templateArgs = templateArgs
            ) { sharingResult, error ->
                if (error != null) {
                    IDormLogger.e(this, "카카오톡 공유 실패: ${error}")
                } else if (sharingResult != null) {
                    IDormLogger.i(
                        this,
                        "카카오톡 공유 성공 ${sharingResult.intent}"
                    )
                    context.startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    IDormLogger.d(
                        this,
                        "Warning Msg: ${sharingResult.warningMsg}"
                    )
                    IDormLogger.d(
                        this,
                        "Argument Msg: ${sharingResult.argumentMsg}"
                    )
                }
            }
        } else {
            val sharerUrl =
                WebSharerClient.instance.makeCustomUrl(
                    templateId,
                    templateArgs
                )
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                try {
                    KakaoCustomTabsClient.open(context, sharerUrl)
                } catch (e: ActivityNotFoundException) {
                    // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                }
            }
        }
    }
}