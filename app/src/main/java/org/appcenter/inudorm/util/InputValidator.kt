package org.appcenter.inudorm.util

fun passwordValidator(value: String): Boolean {
    val pattern =
        "(?=.*\\d{1,15})(?=.*[~`!@#\$%\\^&*()-+=]{1,15})(?=.*[a-z]{2,15}).{8,15}\$".toRegex()
    return value.matches(pattern)
}

fun passwordCharacterValidator(value : String): Boolean {
    val pattern =
        "(?=.*\\d{1,50})(?=.*[~`!@#\$%\\^&*()-+=]{1,50})(?=.*[a-z]{2,50}).{0,50}\$".toRegex()
    return value.matches(pattern)
}

fun emailValidator(value: String): Boolean {
    val pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@inu.ac.kr$".toRegex()
    return value.matches(pattern)
}

