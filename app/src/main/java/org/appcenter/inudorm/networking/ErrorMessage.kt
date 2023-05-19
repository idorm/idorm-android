package org.appcenter.inudorm.networking

import java.io.IOException


enum class StatusCode(private val code: Int) {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500),
    METHOD_NOT_ALLOWED(405),
    PAYLOAD_TOO_LARGE(413),
    UNSUPPORTED_MEDIA_TYPE(415);
}

// Todo: 알 수 없는 에러에 대해 로그 수집하기
class IDormError(val error: ErrorCode) : IOException() {
    val code: String = error.name;
    override val message: String = error.message;

}

/**
 * 메시지는 사용자 친화적으로 바꿀 필요가 있습니다. String Resource로 뺄 계획도 있어요.
 */
enum class ErrorCode(val error: String, val message: String) {

    // Client Side Error
    UNKNOWN_ERROR(StatusCode.INTERNAL_SERVER_ERROR.name, "알 수 없는 오류가 발생했어요."),
    INVALID_REQEUST_PARAM(StatusCode.BAD_REQUEST.name, "잘못된 요청입니다."),

    /**
     * 400 BAD_REQUEST : 잘못된 요청
     */
    FIELD_REQUIRED(StatusCode.BAD_REQUEST.name, "입력은 필수 입니다."),

    // 형식
    EMAIL_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 형식의 이메일이 아닙니다."),
    PASSWORD_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 형식의 비밀번호가 아닙니다."),
    NICKNAME_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 형식의 닉네임이 아닙니다."),
    DORMCATEGORY_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 형식의 기숙사 분류가 아닙니다."),
    JOINPERIOD_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 형식의 입사 기간이 아닙니다."),
    GENDER_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 형식의 성별이 아닙니다."),
    REPORT_TYPE_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 신고 형식이 아닙니다."),
    MEMBER_REPORT_TYPE_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 회원 신고 형식이 아닙니다."),
    COMMUNITY_REPORT_TYPE_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 커뮤니티 신고 형식이 아닙니다."),

    // 사이즈
    NICKNAME_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "닉네임은 2~8자만 가능합니다."),
    PASSWORD_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "비밀번호는 8~15자만 가능합니다."),
    AGE_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "나이는 20~100살 사이여야 합니다."),
    WAKEUPTIME_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "기상 시간은 ~30자 이내로 입력해 주세요."),
    CLEANUPSTATUS_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "정리정돈 상태는 ~30자 이내로 입력해 주세요."),
    SHOWERTIME_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "샤워시간은 ~30자 이내로 입력해 주세요."),
    OPENKAKAOLINK_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "오픈채팅 링크는 ~100자 이내로 입력해 주세요."),
    MBTI_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "MBTI는 3~5자로 입력해 주세요."),
    MBTI_CHARACTER_INVALID(StatusCode.BAD_REQUEST.name, "올바른 MBTI 형식이 아닙니다."),
    WISHTEXT_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "하고싶은 말은 ~150자 이내로 입력해 주세요."),
    CONTENT_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "게시글 혹은 댓글 내용은 1~50자로 입력해 주세요."),
    TITLE_LENGTH_INVALID(StatusCode.BAD_REQUEST.name, "게시글 제목은 1~30자로 입력해 주세요."),

    ILLEGAL_ARGUMENT_ADMIN(StatusCode.BAD_REQUEST.name, "관리자는 해당 요청의 설정 대상이 될 수 없습니다."),
    ILLEGAL_ARGUMENT_SELF(StatusCode.BAD_REQUEST.name, "본인은 해당 요청의 설정 대상이 될 수 없습니다."),

    ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC(StatusCode.BAD_REQUEST.name, "매칭정보가 비공개 입니다."),
    DATE_SET_INVALID(StatusCode.BAD_REQUEST.name, "시작일자가 종료일자보다 빠르거나 같아야 합니다."),

    /**
     * 401 UNAUTHORIZED : 인증되지 않은 사용자
     */
    INVALID_CODE(StatusCode.UNAUTHORIZED.name, "인증번호를 다시 확인해주세요."),
    EXPIRED_CODE(StatusCode.UNAUTHORIZED.name, "인증번호가 만료되었습니다."),
    UNAUTHORIZED_MEMBER(StatusCode.UNAUTHORIZED.name, "로그인이 필요합니다."),
    UNAUTHORIZED_PASSWORD(StatusCode.UNAUTHORIZED.name, "올바르지 않은 비밀번호 입니다."),
    UNAUTHORIZED_EMAIL(StatusCode.UNAUTHORIZED.name, "인증이 되지 않은 이메일입니다."),

    UNAUTHORIZED_POST(StatusCode.UNAUTHORIZED.name, "게시글 수정 및 삭제 권한이 없습니다"),
    UNAUTHORIZED_COMMENT(StatusCode.UNAUTHORIZED.name, "댓글 수정 및 삭제 권한이 없습니다."),
    UNAUTHORIZED_DELETED_MEMBER(StatusCode.UNAUTHORIZED.name, "탈퇴한 회원입니다. 로그아웃이 필요합니다."),

    /**
     * 403 FORBIDDEN : 권한이 없는 사용자
     */
    FORBIDDEN_AUTHORIZATION(StatusCode.FORBIDDEN.name, "접근 권한이 없습니다."),

    /**
     * 404 NOT_FOUND : Resource 를 찾을 수 없음
     */
    EMAIL_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 이메일이 없습니다."),
    MEMBER_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 멤버가 없습니다."),
    DISLIKEDMEMBER_NOT_FOUND(StatusCode.NOT_FOUND.name, "싫어요한 멤버가 없습니다."),
    LIKEDMEMBER_NOT_FOUND(StatusCode.NOT_FOUND.name, "좋아요한 멤버가 없습니다."),
    FILE_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 파일이 없습니다."),
    MATCHINGINFO_NOT_FOUND(StatusCode.NOT_FOUND.name, "룸메이트 매칭을 위해서\n" +
            "우선 매칭 이미지를 만들어 주세요."),
    COMMENT_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 댓글이 없습니다."),
    POSTLIKEDMEMBER_NOT_FOUND(StatusCode.NOT_FOUND.name, "멤버가 게시글에 공감하지 않았습니다."),
    POST_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 게시글이 없습니다."),
    POSTPHOTO_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 게시글 사진이 없습니다."),
    MEMBERPHOTO_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 프로필 사진이 없습니다."),
    LIKED_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 공감이 없습니다."),
    CALENDAR_NOT_FOUND(StatusCode.NOT_FOUND.name, "등록된 일정 정보가 없습니다."),

    DELETED_POST(StatusCode.NOT_FOUND.name, "삭제된 게시글 입니다."),
    DELETED_COMMENT(StatusCode.NOT_FOUND.name, "삭제된 댓글 입니다."),

    /**
     * 405 METHOD_NOT_ALLOWED : 대상 리소스가 해당 메서드를 지원하지 않음
     */
    METHOD_NOT_ALLOWED(StatusCode.METHOD_NOT_ALLOWED.name, "지원하지 않는 메서드입니다."),

    /**
     * 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
     */

    DUPLICATE_EMAIL(StatusCode.CONFLICT.name, "이미 등록된 이메일 입니다."),
    DUPLICATE_MEMBER(StatusCode.CONFLICT.name, "이미 등록된 멤버 입니다."),
    DUPLICATE_NICKNAME(StatusCode.CONFLICT.name, "이미 존재하는 닉네임 입니다."),
    DUPLICATE_SAME_NICKNAME(StatusCode.CONFLICT.name, "기존의 닉네임과 같습니다."),
    DUPLICATE_MATCHINGINFO(StatusCode.CONFLICT.name, "매칭정보가 이미 등록되어 있습니다."),
    DUPLICATE_LIKED_MEMBER(StatusCode.CONFLICT.name, "이미 좋아요한 멤버 입니다."),
    DUPLICATE_DISLIKED_MEMBER(StatusCode.CONFLICT.name, "이미 싫어요한 멤버 입니다."),
    DUPLICATE_LIKED(StatusCode.CONFLICT.name, "공감은 한 번만 가능합니다."),

    CANNOT_UPDATE_NICKNAME(StatusCode.CONFLICT.name, "닉네임은 30일마다 변경할 수 있습니다."),
    CANNOT_LIKED_SELF(StatusCode.CONFLICT.name, "본인의 글에 공감할 수 없습니다."),
    CANNOT_LIKED_POST_BY_DELETED_MEMBER(StatusCode.CONFLICT.name, "게시글 작성자가 탈퇴한 글은 공감할 수 없습니다."),
    MEMBER_CANNOT_SELFREPORT(StatusCode.CONFLICT.name, "본인은 신고할 수 없습니다."),
    POST_CANNOT_SELFREPORT(StatusCode.CONFLICT.name, "본인의 게시글은 신고할 수 없습니다."),
    COMMENT_CANNOT_SELFREPORT(StatusCode.CONFLICT.name, "본인의 댓글은 신고할 수 없습니다."),

    /**
     * 413 PAYLOAD_TOO_LARGE
     */
    FILE_SIZE_EXCEED(StatusCode.PAYLOAD_TOO_LARGE.name, "파일 용량이 초과되었습니다"),
    FILE_COUNT_EXCEED(StatusCode.PAYLOAD_TOO_LARGE.name, "파일 개수가 초과되었습니다"),

    /**
     * 415 UNSUPPORTED_MEDIA_TYPE
     */
    FILE_TYPE_UNSUPPORTED(
        StatusCode.UNSUPPORTED_MEDIA_TYPE.name,
        "파일 형식은 '.jpg', '.jpeg', '.png' 만 가능합니다."
    ),

    /**
     * 500 SERVER_ERROR : 서버 에러
     */
    SERVER_ERROR(StatusCode.INTERNAL_SERVER_ERROR.name, "서버 에러가 발생했습니다."),
    EMAIL_SENDING_ERROR(StatusCode.INTERNAL_SERVER_ERROR.name, "이메일 전송 중에 서버 에러가 발생했습니다."),
    FIREBASE_SERER_ERROR(StatusCode.INTERNAL_SERVER_ERROR.name, "푸시 알람 전송 중에 서버 에러가 발생했습니다."),
    S3_SERVER_ERROR(StatusCode.INTERNAL_SERVER_ERROR.name, "S3 사진 저장 중에 서버 에러가 발생했습니다."),


}