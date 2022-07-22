package org.appcenter.inudorm.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.constraintlayout.solver.widgets.analyzer.Direct
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentMatchingBinding
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter


class MatchingFragment : Fragment(), CardStackListener {

    private val blue = Color.parseColor("#582FFF")
    private val green = Color.parseColor("#00FF57")
    private val yellow = Color.parseColor("#FFC701")
    private val red = Color.parseColor("#FF0000")
    private val TAG = "MatchingFragment"

    companion object {
        fun newInstance() = MatchingFragment()
    }

    private val viewModel: MatchingViewModel by viewModels()
    private lateinit var binding: FragmentMatchingBinding
    private lateinit var layoutManager: CardStackLayoutManager

    private fun setupCardStackView() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setInterpolator(AccelerateInterpolator())
            .build()
        layoutManager = CardStackLayoutManager(activity, this).apply {
            setStackFrom(StackFrom.Bottom)
            setScaleInterval(1.0F)
            setSwipeAnimationSetting(setting)
            setCanScrollVertical(false)
            setVisibleCount(4)
        }

        val dataSet = ArrayList<String>()
        dataSet.add("지혜원")
        dataSet.add("최경민")
        dataSet.add("최경민")
        dataSet.add("최경민")
        binding.cardStackView.setOnDragListener { v, event ->
            Log.d(TAG, event.x.toString())
            true
        }
        binding.cardStackView.layoutManager = layoutManager
        binding.cardStackView.adapter = RoomMateAdapter(dataSet)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matching, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupCardStackView()
        setupControlButton()
    }

    private fun getSwipeAnimationSetting(direction: Direction): SwipeAnimationSetting =
        SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()

    private fun setupControlButton() {
        binding.dislikeButton.setOnClickListener {
            layoutManager.setSwipeAnimationSetting(getSwipeAnimationSetting(Direction.Left))
            binding.cardStackView.swipe()
            lifecycleScope.launch {
                animateToColor(red, 150)
            }

        }
        binding.chatButton.setOnClickListener {
            lifecycleScope.launch {
                animateToColor(yellow, 150)
                animateToColor(blue, 150)
            }
        }
        binding.likeButton.setOnClickListener {
            layoutManager.setSwipeAnimationSetting(getSwipeAnimationSetting(Direction.Right))
            binding.cardStackView.swipe()
            lifecycleScope.launch { animateToColor(green, 150) }

        }
    }

    private fun directionToColor(direction: Direction): Int {
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


    override fun onCardDragging(direction: Direction?, ratio: Float) {
        val rgbEval = ArgbEvaluator().evaluate(
            ratio,
            blue,
            directionToColor(direction!!)
        ) as Int
        binding.circle.setBackgroundColor(rgbEval)
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

    private suspend fun animateToColor(
        colorTo: Int,
        duration: Long,
    ) {
        val colorFrom = (binding.circle.background as ColorDrawable).color
        animateColors(colorFrom, colorTo, duration) {
            binding.circle.setBackgroundColor(it)
        }
        delay(duration)
    }

    override fun onCardSwiped(direction: Direction?) {
        Toast.makeText(requireContext(), direction.toString(), Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            animateToColor(blue, 250)
        }

    }

    override fun onCardRewound() {}
    override fun onCardCanceled() {
        lifecycleScope.launch {
            animateToColor(blue, 150)

        }
    }

    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}

}