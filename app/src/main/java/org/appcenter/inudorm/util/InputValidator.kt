package org.appcenter.inudorm.util

fun passwordValidator(value: String): Boolean {
    val pattern =
        "(?=.*\\d{1,15})(?=.*[~`!@#\$%\\^&*()-+=]{1,15})(?=.*[a-z]{2,15}).{8,15}\$".toRegex()
    return value.matches(pattern)
}

fun passwordCharacterValidator(value: String): Boolean {
    val pattern =
        "(?=.*\\d{1,50})(?=.*[~`!@#\$%\\^&*()-+=]{1,50})(?=.*[a-z]{2,50}).{0,50}\$".toRegex()
    return value.matches(pattern)
}

fun emailValidator(value: String): Boolean {
    val pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@inu.ac.kr$".toRegex()
    return value.matches(pattern)
}

fun urlExtractor(value: String): String? {
    val pattern =
        "(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])".toRegex()
    if (value.contains(pattern))
        return pattern.find(value)?.value
    return null
}

fun hostExtractor(value: String): String {
    val pattern =
        "(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])".toRegex()
    return pattern.find(value)?.groupValues?.get(2) ?: ""
}
