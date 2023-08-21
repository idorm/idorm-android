package org.appcenter.inudorm.presentation.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentMyPageBinding
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.LoadingFragment
import org.appcenter.inudorm.presentation.MainActivity
import org.appcenter.inudorm.presentation.mypage.community.CommentListActivity
import org.appcenter.inudorm.presentation.mypage.community.liked_post_list.LikedPostListActivity
import org.appcenter.inudorm.presentation.mypage.community.wrote_post_list.WrotePostListActivity
import org.appcenter.inudorm.presentation.mypage.matching.DisLikedMateListActivity
import org.appcenter.inudorm.presentation.mypage.matching.LikedMateListActivity
import org.appcenter.inudorm.presentation.mypage.matching.MyMatchingProfileActivity
import org.appcenter.inudorm.presentation.mypage.myinfo.MyInfoSettingActivity
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.OkDialog
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor

class MyPageFragment : LoadingFragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private lateinit var viewModel: MyPageViewModel
    private lateinit var binding: FragmentMyPageBinding
    private val prefsRepository: PrefsRepository by lazy {
        PrefsRepository(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.iDorm_blue
            )
        )
        return binding.root
    }

    private fun showErrorDialog(context: Context, text: String, onOk: (() -> Unit)? = null) {
        CustomDialog(text, positiveButton = DialogButton("확인", onClick = onOk)).show(context)
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

    fun openPostsWrote() {
        startActivity(Intent(requireContext(), WrotePostListActivity::class.java))
    }

    fun openCommentsWrote() {
        startActivity(Intent(requireContext(), CommentListActivity::class.java))
    }

    fun openPostsLiked() {
        startActivity(Intent(requireContext(), LikedPostListActivity::class.java))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
                viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = requireActivity()

        viewModel.getMyMatchingInfo()


        lifecycleScope.launch {
            viewModel.myPageState.collect {
                if (it.myInfo?.isError() == true || it.matchingInfo?.isError() == true)
                    this@MyPageFragment.setLoadingState(false)
                this@MyPageFragment.setLoadingState(
                    it.myInfo?.loading == true || it.matchingInfo?.loading == true
                )
                IDormLogger.i(this@MyPageFragment, "state changed: $it")
                if (viewModel.myPageState.value.matchingInfo?.error == null && !viewModel.myPageState.value.matchingInfo?.loading!!) {
                    IDormLogger.i(this@MyPageFragment, "가능")
                }
                if (it.myInfo?.isError() == true) {
                    UIErrorHandler.handle(
                        this@MyPageFragment.requireContext(),
                        prefsRepository,
                        it.myInfo.error!!
                    ) { e ->
                        IDormLogger.i(this@MyPageFragment, "Error Handler called")
                        when (e.error) {
                            else -> {
                                OkDialog(e.error.message)
                            }
                        }
                    }
                }
            }
        }

        binding.settingButton.setOnClickListener {
            val intent =
                Intent(this@MyPageFragment.requireContext(), MyInfoSettingActivity::class.java)
            startActivity(intent)
        }

        binding.matchingImageButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MyPageFragment.requireContext(),
                    MyMatchingProfileActivity::class.java
                )
            )

        }

        binding.dislikedRoomMatesButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MyPageFragment.requireContext(),
                    DisLikedMateListActivity::class.java
                )
            )
        }

        binding.likedRoomMatesButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MyPageFragment.requireContext(),
                    LikedMateListActivity::class.java
                )
            )
        }
    }
}