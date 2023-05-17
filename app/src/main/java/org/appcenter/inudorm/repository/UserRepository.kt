package org.appcenter.inudorm.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.appcenter.inudorm.App.Companion.gson
import org.appcenter.inudorm.model.*
import org.appcenter.inudorm.model.board.Photo
import org.appcenter.inudorm.networking.RetrofitInstance
import org.appcenter.inudorm.networking.createJsonRequestBody
import org.appcenter.inudorm.usecase.CodeVerifyParams
import org.appcenter.inudorm.usecase.RegisterParams
import org.appcenter.inudorm.usecase.SendAuthCodeParams
import org.appcenter.inudorm.usecase.UserInputParams
import retrofit2.Response

class UserRepository {
    suspend fun login(params: UserInputParams?, fcmKey: String): Response<User> {
        val str = gson.toJson(params)
        return RetrofitInstance.service.login(createJsonRequestBody(str), fcmKey)
    }


    suspend fun loginRefresh(): User {
        return RetrofitInstance.service.loginRefresh()
    }

    suspend fun sendRegisterAuthCode(params: SendAuthCodeParams) {
        val str = gson.toJson(params)
        return RetrofitInstance.service.sendRegisterEmail(createJsonRequestBody(str))
    }

    suspend fun sendPasswordAuthCode(params: SendAuthCodeParams) {
        val str = gson.toJson(params)
        return RetrofitInstance.service.sendForgotPWEmail(createJsonRequestBody(str))
    }

    suspend fun verifyRegisterCode(params: CodeVerifyParams): EmailVerifyResponseDto {
        val str = gson.toJson(params)
        RetrofitInstance.service.verifyRegisterCode(params.email, createJsonRequestBody(str))
        return EmailVerifyResponseDto(params.email, true, true)
    }

    suspend fun verifyPasswordAuthCode(params: CodeVerifyParams): EmailVerifyResponseDto {
        val str = gson.toJson(params)
        RetrofitInstance.service.verifyForgotPWEmail(params.email, createJsonRequestBody(str))
        return EmailVerifyResponseDto(params.email, true, true)
    }

    suspend fun register(params: RegisterParams): User {
        val str = gson.toJson(params)
        return RetrofitInstance.service.register(createJsonRequestBody(str))
    }

    suspend fun withdraw(): Boolean {
        return RetrofitInstance.service.withdrawMember().isSuccessful
    }

    suspend fun changeNickName(params: ChangeNickNameDto) {
        return RetrofitInstance.service.changeNickname(params)
    }


    suspend fun changePassword(params: ChangePasswordDto): User {
        return RetrofitInstance.service.changePassword(params)
    }

    suspend fun updateFcmToken(token: String) {
        RetrofitInstance.service.updateFcmToken(token)
    }


    suspend fun report(body: ReportRequestDto) {
        return RetrofitInstance.service.report(body)
    }

    suspend fun updateProfilePhoto(photo: Photo?) {
        val requestFile = photo?.file?.asRequestBody("image/jpg".toMediaTypeOrNull())
            ?: throw java.lang.RuntimeException("이미지 RequestBody 변환 실패.")
        return RetrofitInstance.service.updateProfilePhoto(
            MultipartBody.Part.createFormData("file", photo?.image?.name, requestFile)
        )
    }

    suspend fun deleteProfilePhoto() {
        return RetrofitInstance.service.deleteProfilePhoto();
    }

    suspend fun signOut() {
        return RetrofitInstance.service.signOut();
    }

}