package org.appcenter.inudorm.util

fun passwordValidator(value:String) : Boolean {
    val pattern = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{6,15}\$".toRegex()
    return value.matches(pattern)
}

fun emailValidator(value:String) : Boolean {
    val pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@inu.ac.kr$".toRegex()
    return value.matches(pattern)
}