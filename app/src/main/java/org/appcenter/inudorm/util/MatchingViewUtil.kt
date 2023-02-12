package org.appcenter.inudorm.util

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import kotlinx.coroutines.delay
import org.appcenter.inudorm.R

class MatchingViewUtil(val context: Activity) {
    val blue = ContextCompat.getColor(context, R.color.iDorm_blue)
    val green = ContextCompat.getColor(context, R.color.iDorm_green)
    val yellow = ContextCompat.getColor(context, R.color.iDorm_yellow)
    val red = ContextCompat.getColor(context, R.color.iDorm_red)

    fun getSwipeAnimationSetting(direction: Direction): SwipeAnimationSetting =
        SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()


    fun directionToColor(direction: Direction): Int {
        return when (direction) {
            Direction.Left -> {
                red
            }
            Direction.Right -> {
                green
            }
            else -> {
                blue
            }
        }
    }

    // 추후 다른 곳에서 필요로 하면 다른 모듈로 뺄 예정. 지금은 코드의 복잡성을 줄이기 위해 빼놓음
    private fun animateColors(
        colorFrom: Int,
        colorTo: Int,
        duration: Long,
        onValueUpdate: (value: Int) -> Unit
    ) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = duration // milliseconds

        colorAnimation.addUpdateListener { animator -> onValueUpdate(animator.animatedValue as Int) }
        colorAnimation.start()
    }

    suspend fun animateToColor(
        circle: FrameLayout,
        colorTo: Int,
        duration: Long,
    ) {
        val colorFrom = (circle.background as ColorDrawable).color
        animateColors(colorFrom, colorTo, duration) {
            circle.setBackgroundColor(it)
            context.window.statusBarColor = it
        }
        delay(duration)
    }
}