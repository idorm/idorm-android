package org.appcenter.inudorm.model

import com.google.gson.annotations.SerializedName

// Todo: Complete Member class

/**
 * 사용자/메이트를 추상화한 Member 클래스입니다.
 * ⚠ 이 클래스를 생성하시면 안됩니다!
 */
abstract class Member(
    open val id: Long,
    open val email: String? = null,
    open val nickname: String,
    open val profileImage: String?,
    val likedMem: List<String>? = null,
    open val myInfo: MatchingInfo? = null,
    val matching: String? = null,
    val matchings: String? = null,
    val comment: String? = null,
    val subComment: String? = null,
)


data class User(
    val email: String,
    val nickname: String,
    val profilePhotoUrl: String?,
    @SerializedName("matchingInfoId")
    val matchingInfo: Int?,
    var loginToken: String?
)


/**
 * Member 클래스를 상속받은 Mate 클래스입니다.
 * id: 메이트의 identifier
 */
data class Mate(
    override val id: Long,
    override val nickname: String,
    override val profileImage: String?,
    override val myInfo: MatchingInfo,
) :
    Member(id = id, nickname = nickname, profileImage = profileImage, myInfo = myInfo)