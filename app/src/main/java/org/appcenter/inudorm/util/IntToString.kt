package org.appcenter.inudorm.util

fun addZeroToMakeLength(value:Int, length: Int) : String{
    var string = value.toString()
    while (string.length < length) {
        string = "0$string"
    }
    return string
}