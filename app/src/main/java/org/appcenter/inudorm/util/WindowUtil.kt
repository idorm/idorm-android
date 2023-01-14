package org.appcenter.inudorm.util

import android.graphics.Color
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowInsetsControllerCompat
import org.appcenter.inudorm.R

object WindowUtil {
    fun AppCompatActivity.setStatusBarColor(color: String) {
        this.window.statusBarColor = color.toColorInt()
        val window: Window = window
        IDormLogger.i(this, "$color, ${isLightColor(color)}")
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

    }

    fun isLightColor(color: String): Boolean {
        val number = color.substring(1)
        val r = number.substring(0, 2).toInt()
        val g = number.substring(2, 4).toInt()
        val b = number.substring(4, 6).toInt()
        return (r + g + b) / 3 > 125.5
    }
}