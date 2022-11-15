package org.appcenter.inudorm.networking

import org.appcenter.inudorm.presentation.account.EmailPromptPurpose
import org.appcenter.inudorm.util.IDormLogger
import retrofit2.HttpException
import kotlin.reflect.KProperty0

object ErrorMessage {
    const val unknownError: String = "알 수 없는 오류입니다."

    val EmailVerify: Map<EmailPromptPurpose, KProperty0<Map<Int, String>>> = mapOf(
        EmailPromptPurpose.Register to ErrorMessage::RegisterEmailVerify,
        EmailPromptPurpose.FindPass to ErrorMessage::FindPasswordEmailVerify
    )

    val RegisterEmailVerify: Map<Int, String> = mapOf(
        401 to "인증번호가 만료되었습니다.",
        404 to "등록 혹은 가입되지 않은 이메일입니다.",
        409 to "잘못된 인증번호입니다.",
    )

    val FindPasswordEmailVerify: Map<Int, String> = mapOf(
        409 to "잘못된 인증번호입니다.",
        401 to "인증에 실패했습니다.",
        404 to "인증번호가 만료되었습니다."
    )

    val Login: Map<Int, String> = mapOf(
        400 to "이메일 또는 비밀번호를 입력해주세요.",
        409 to "올바르지 않은 비밀번호입니다.",
        404 to "가입되지 않은 이메일입니다.",
    )

    val Register: Map<Int, String> = mapOf(
        409 to "이미 가입된 이메일입니다.",
        400 to "올바른 형식의 이메일 주소여야 합니다.",
        404 to "등록하지 않은 이메일입니다."
    )

    val EmailSend: Map<Int, String> = mapOf(
        409 to "잘못된 인증번호입니다.",
        401 to "인증에 실패했습니다.",
        404 to "인증번호가 만료되었습니다."
    )

    /**
     * @param e 예외처리할 에러
     * @param map http 에러코드에 따라 에러처리 할 맵
     */
    fun message(e: Throwable, map: KProperty0<Map<Int, String>>): String {
        IDormLogger.d(this, e.message!!)
        val message = if (e is HttpException) {
            map.get()[e.code()] ?: unknownError
        } else unknownError
        return message
    }
}