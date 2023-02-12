package org.appcenter.inudorm

interface OnSnackBarCallListener {
    fun onSnackBarCalled(message:String, duration:Int)
    fun openSnackBar(message:String, duration:Int) = onSnackBarCalled(message, duration)
}