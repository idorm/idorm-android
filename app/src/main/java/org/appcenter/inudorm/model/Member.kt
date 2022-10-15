package org.appcenter.inudorm.model

// Todo: Complete Member class

/**
 * 사용자/메이트를 추상화한 Member 클래스입니다.
 * ⚠ 이 클래스를 생성하시면 안됩니다!
 */
abstract class Member(
    open val id: Long,
    open val email: String? = null,
    open val nickname: String,
    open val profileImage: String,
    val likedMem: List<String>? = null,
    open val myInfo: MyInfo? = null,
    val matching: String? = null,
    val matchings: String? = null,
    val comment: String? = null,
    val subComment: String? = null,
)

/**
 * Member 클래스를 상속받은 Mate 클래스입니다.
 * id: 메이트의 identifier
 *
 */
data class Mate(
    override val id: Long,
    override val nickname: String,
    override val profileImage: String,
    override val myInfo: MyInfo,
) :
    Member(id = id, nickname = nickname, profileImage = profileImage, myInfo = myInfo)