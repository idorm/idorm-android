package org.appcenter.inudorm

import android.os.Bundle

interface OnSnackBarCallListener {
    fun onSnackBarCalled(message:String, duration:Int)
}