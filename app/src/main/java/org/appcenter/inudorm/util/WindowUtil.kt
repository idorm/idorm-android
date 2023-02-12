package org.appcenter.inudorm.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.roundToInt

object WindowUtil {
    // 색상을 받아 밝은 색 계열이면 흰색으로 전환합니다.
    fun Activity.setStatusBarColor(@ColorInt color: Int) {
        this.window.statusBarColor = color
        val window: Window = window
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            !isLightColor(color) || color == Color.WHITE
    }

    fun isLightColor(color: Int): Boolean {
        val colorString = String.format("%06X", (0xFFFFFF and color))
        val r = Integer.parseInt(colorString.substring(0, 2), 16)
        val g = Integer.parseInt(colorString.substring(2, 4), 16)
        val b = Integer.parseInt(colorString.substring(4, 6), 16)
        return (r + g + b) / 3 > 125.5
    }

    fun pxToDp(context: Context, px: Float): Float {
        var density = context.resources.displayMetrics.density
        when (density) {
            1.0f -> density *= 4.0f
            1.5f -> density *= (8 / 3)
            2.0f -> density *= 2.0f
        }
        return px / density
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val dm = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), dm).roundToInt()
    }
}