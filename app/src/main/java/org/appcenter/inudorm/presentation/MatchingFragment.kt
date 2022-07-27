package org.appcenter.inudorm.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import androidx.recyclerview.widget.RecyclerView
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentMatchingBinding
import org.appcenter.inudorm.model.Mate
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.repository.RoomMateRepository
import org.appcenter.inudorm.repository.testMate
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.MatchingViewUtil

class MatchingFragment : Fragment(), CardStackListener {

    private val TAG = "MatchingFragment"

    companion object {
        fun newInstance() = MatchingFragment()
    }

    private val roomMateRepository = RoomMateRepository()
    private val viewModel: MatchingViewModel by viewModels {
        MatchingViewModelFactory(roomMateRepository)
    }
    private lateinit var binding: FragmentMatchingBinding
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var adapter: RoomMateAdapter
    private val matchingViewUtil by lazy {
        MatchingViewUtil(requireContext())
    }

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

        adapter = RoomMateAdapter(ArrayList())

        binding.cardStackView.layoutManager = layoutManager
        binding.cardStackView.adapter = adapter
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
        binding.matchingViewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getMates(10)
    }

    private fun animateColorAndRestore(color: Int, duration: Long) {
        lifecycleScope.launch {
            matchingViewUtil.animateToColor(binding.circle, color, duration)
            matchingViewUtil.animateToColor(binding.circle, matchingViewUtil.blue, duration)
        }
    }


    private fun setupControlButton() {
        binding.dislikeButton.setOnClickListener {
            layoutManager.setSwipeAnimationSetting(
                matchingViewUtil.getSwipeAnimationSetting(
                    Direction.Left
                )
            )
            binding.cardStackView.swipe()
            animateColorAndRestore(matchingViewUtil.red, 150)
        }
        binding.chatButton.setOnClickListener {
            animateColorAndRestore(matchingViewUtil.yellow, 150)
            // Todo: Add KakaoTalk Icon to button
            CustomDialog(
                text = "상대의 카카오톡 오픈채팅으로 이동합니다.",
                positiveButton = DialogButton("카카오톡으로 이동", {
                    val position = layoutManager.topPosition
                    val link = adapter.dataSet[position].myInfo.chatLink
                    if (link == null) {
                        Toast.makeText(
                            requireContext(),
                            "카카오톡 오픈채팅 링크를 찾을 수 없어요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                })
            ).show(requireContext())
        }
        binding.likeButton.setOnClickListener {
            layoutManager.setSwipeAnimationSetting(
                matchingViewUtil.getSwipeAnimationSetting(
                    Direction.Right
                )
            )
            binding.cardStackView.swipe()
            animateColorAndRestore(matchingViewUtil.green, 150)
        }
    }


    override fun onCardDragging(direction: Direction?, ratio: Float) {
        val rgbEval = ArgbEvaluator().evaluate(
            ratio,
            matchingViewUtil.green,
            matchingViewUtil.directionToColor(direction!!)
        ) as Int
        binding.circle.setBackgroundColor(rgbEval)
    }


    override fun onCardSwiped(direction: Direction?) {
        Toast.makeText(requireContext(), direction.toString(), Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            matchingViewUtil.animateToColor(binding.circle, matchingViewUtil.blue, 250)
        }

    }

    override fun onCardRewound() {}
    override fun onCardCanceled() {
        lifecycleScope.launch {
            matchingViewUtil.animateToColor(binding.circle, matchingViewUtil.blue, 150)
        }
    }

    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}

}