package org.appcenter.inudorm.presentation.matching

import android.animation.ArgbEvaluator
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentMatchingBinding
import org.appcenter.inudorm.model.MatchingInfo
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.RoomMateAdapter
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.*
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor

const val FILTER_RESULT_CODE = 1226

class MatchingFragment : Fragment(), CardStackListener {

    private val TAG = "MatchingFragment"

    private val viewModel: MatchingViewModel by viewModels {
        MatchingViewModelFactory()
    }
    private lateinit var binding: FragmentMatchingBinding
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var adapter: RoomMateAdapter
    private val matchingViewUtil by lazy {
        MatchingViewUtil(requireActivity())
    }
    private val prefsRepository: PrefsRepository by lazy {
        PrefsRepository(requireContext())
    }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private fun CardStackView.swipeTo(direction: Direction) {
        (this.layoutManager as CardStackLayoutManager).setSwipeAnimationSetting(
            matchingViewUtil.getSwipeAnimationSetting(
                direction
            )
        )
        binding.cardStackView.swipe()
        when (direction) {
            Direction.Left -> animateColorAndRestore(matchingViewUtil.red, 150)
            Direction.Right -> animateColorAndRestore(matchingViewUtil.green, 150)
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.iDorm_blue
            )
        )
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

        adapter = RoomMateAdapter(true, ArrayList()) {
            val modalBottomSheet = ListBottomSheet(
                arrayListOf(
                    SelectItem(
                        "신고하기",
                        "report",
                        desc = getString(R.string.matchingImageViolence)
                    )
                )
            ) {
                if (it.value == "report") {
                    CustomDialog(
                        "게시글을 신고하고 싶으신가요?",
                        positiveButton = DialogButton("확인", onClick = {
                            viewModel.reportMatchingInfo(getCurrentItem().matchingInfoId)
                        })
                    ).show(this@MatchingFragment.requireContext())
                }
            }
            modalBottomSheet.show(
                requireActivity().supportFragmentManager,
                ListBottomSheet.TAG
            )
        }

        binding.cardStackView.layoutManager = layoutManager
        binding.cardStackView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_matching, container, false)
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.iDorm_blue
            )
        )
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.iDorm_blue
            )
        )
        setupFilter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupCardStackView()
        setupControlButton()
        binding.matchingViewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        viewModel.getMates(LoadMode.Update, size = 10)
        lifecycleScope.launch {
            viewModel.userMutationEvent.collect { state ->
                when (state) {
                    is UserMutationEvent.AddLikedMatchingInfo -> {
                        if (state.success == false)
                            binding.cardStackView.rewind()
                    }
                    is UserMutationEvent.AddDislikedMatchingInfo -> {
                        if (state.success == false)
                            binding.cardStackView.rewind()
                    }
                    is UserMutationEvent.ReportMatchingInfo -> {
                        if (state.success == true) CustomDialog(
                            "사용자를 신고했습니다.",
                            positiveButton = DialogButton("확인")
                        ).show(
                            this@MatchingFragment.requireContext()
                        )
                        else CustomDialog(
                            "사용자 신고에 실패했습니다.",
                            positiveButton = DialogButton("확인")
                        ).show(this@MatchingFragment.requireContext())
                    }
                    is UserMutationEvent.DeleteLikedMatchingInfo -> {
                        if (state.success == true) binding.cardStackView.rewind()
                    }
                    is UserMutationEvent.DeleteDislikedMatchingInfo -> {
                        if (state.success == true) binding.cardStackView.rewind()
                    }
                    else -> {}
                }
            }
        }
        // StateFlow 를 watching 해서 무언가를 호출해야 하는 상황이면 차라리 Activity/Fragment 코드에 해주는게 나을 것 같음.
        // 왜냐? View 에 바인딩 하게 되면 주제가 안맞음.
        lifecycleScope.launch {
            viewModel.matchingState.collect {
                // Todo: ⚠️ 네트워크 오류로 퉁치지 말고 꼭!! 상황별 에러 대응하게 처리할 것!!!
                if (it.error != null) {
                    UIErrorHandler.handle(requireContext(), prefsRepository, it.error!!) { err ->
                        when (err.error) {
                            ErrorCode.DUPLICATE_DISLIKED_MEMBER -> {}
                            ErrorCode.DUPLICATE_LIKED_MEMBER -> {}
                            ErrorCode.MATCHINGINFO_NOT_FOUND -> {
                                // Todo: 매칭정보 비공개. 다이얼로그 띄워서 온보딩으로 연결
                                CustomDialog(
                                    "룸메이트 매칭을 위해\n우선 매칭 이미지를 만들어 주세요.",
                                    positiveButton = DialogButton(
                                        "프로필 이미지 만들기",
                                        ButtonType.Filled,
                                        onClick = {
                                            // Todo: 온보딩 연결!!
                                        })
                                ).show(this@MatchingFragment.requireContext())
                            }
                            ErrorCode.ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC -> {
                                CustomDialog(
                                    "룸메이트 매칭을 위해\n" +
                                            "우선 내 매칭 이미지를\n" +
                                            "매칭페이지에 공개해야 해요.",
                                    positiveButton = DialogButton("취소"),
                                    negativeButton = DialogButton("공개 허용", onClick = {
                                        // Todo: 매칭정보 공개 API
                                        viewModel.setMatchingInfoVisibility(true)
                                    })
                                ).show(this@MatchingFragment.requireContext())
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setupFilter() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.i(TAG, "activity result: ${result.resultCode} ${result.data}")
                if (result.resultCode == FILTER_RESULT_CODE) {
                    val intent = result.data
                    val filter = intent?.getParcelableExtra<RoomMateFilter>("filter")
                    viewModel.getMates(
                        LoadMode.Update,
                        filter ?: viewModel.matchingState.value.filter,
                        10
                    )
                    Log.i(TAG, "result filter:  ${filter}")
                }
            }
    }

    private fun animateColorAndRestore(color: Int, duration: Long) {
        lifecycleScope.launch {
            matchingViewUtil.animateToColor(binding.circle, color, duration)
            matchingViewUtil.animateToColor(binding.circle, matchingViewUtil.blue, duration)
        }
    }

    private fun setupControlButton() {
        binding.dislikeButton.setOnClickListener {
            binding.cardStackView.swipeTo(Direction.Left)
        }
        binding.likeButton.setOnClickListener {
            binding.cardStackView.swipeTo(Direction.Right)
        }

        binding.backButton.setOnClickListener {
            if (layoutManager.topPosition > 0)
                when (viewModel.userMutationEvent.value) {
                    is UserMutationEvent.AddDislikedMatchingInfo -> {
                        viewModel.deleteDislikedMate(getCurrentItem(1).memberId)
                    }
                    is UserMutationEvent.AddLikedMatchingInfo -> {
                        viewModel.deleteLikedMate(getCurrentItem(1).memberId)
                    }
                    else -> {}
                } else Toast.makeText(
                this@MatchingFragment.requireContext(),
                "처음 카드에요.",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.chatButton.setOnClickListener {
            animateColorAndRestore(matchingViewUtil.yellow, 150)
            // Todo: Add KakaoTalk Icon to button
            CustomDialog(
                text = "상대의 카카오톡 오픈채팅으로 이동합니다.",
                positiveButton = DialogButton(
                    "카카오톡으로 이동",
                    icon = R.drawable.ic_kakaotalk_logo,
                    onClick = {
                        val link = getCurrentItem().openKakaoLink
                        IDormLogger.i(this, "Open link: $link")
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            startActivity(intent)
                        } catch (_: ActivityNotFoundException) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.kakaoLinkNotFound),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, buttonType = ButtonType.Filled
                )
            ).show(requireContext())
        }
        binding.openFilterButton.setOnClickListener {
            val intent = Intent(
                this@MatchingFragment.activity?.applicationContext,
                FilterActivity::class.java
            )
            intent.putExtra("filter", viewModel.matchingState.value.filter)
            activityResultLauncher.launch(intent)
        }
    }


    override fun onCardDragging(direction: Direction?, ratio: Float) {
        val rgbEval = ArgbEvaluator().evaluate(
            ratio,
            matchingViewUtil.green,
            matchingViewUtil.directionToColor(direction!!)
        ) as Int
        binding.circle.setBackgroundColor(rgbEval)
        requireActivity().window.statusBarColor = rgbEval
    }

    private fun getCurrentItem(offset: Int = 0): MatchingInfo {
        val position = layoutManager.topPosition
        return adapter.dataSet[position - offset]
    }


    override fun onCardSwiped(direction: Direction?) {
        Toast.makeText(requireContext(), direction.toString(), Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            matchingViewUtil.animateToColor(binding.circle, matchingViewUtil.blue, 250)
        }
        when (direction) {
            Direction.Left -> viewModel.addDislikedMate(getCurrentItem(1).memberId)
            Direction.Right -> viewModel.addLikedMate(getCurrentItem(1).memberId)
            else -> {}
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

    override fun onDetach() {
        requireActivity().window.statusBarColor = Color.WHITE
        super.onDetach()
    }

}