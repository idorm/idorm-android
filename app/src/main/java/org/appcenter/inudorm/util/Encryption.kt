package org.appcenter.inudorm.util

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.DigestException
import java.security.MessageDigest

fun encrypt(value:String):String {
    val hash:String
    try {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(value.toByteArray(StandardCharsets.UTF_8))
        val encrypted = messageDigest.digest()
        hash = Base64.encodeToString(encrypted, Base64.NO_WRAP)
    } catch (e: CloneNotSupportedException) {
        throw DigestException("Couldn't make digest of partial content")
    }

    return hash
}