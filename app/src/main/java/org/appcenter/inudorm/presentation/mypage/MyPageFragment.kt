package org.appcenter.inudorm.presentation.mypage

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.App.Companion.userRepository
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentMyPageBinding
import org.appcenter.inudorm.networking.ErrorCode
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.IDormLogger
import kotlin.math.E

class MyPageFragment : Fragment() {

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
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.iDorm_blue)
        return binding.root
    }

    private fun showErrorDialog(context: Context, text: String, onOk: (() -> Unit)? = null) {
        CustomDialog(text, positiveButton = DialogButton("확인", onClick = onOk)).show(context)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.iDorm_blue)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getMyMatchingInfo()

        lifecycleScope.launch {
            viewModel.myPageState.collect {
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
                        when (e.error) {
                            ErrorCode.UNAUTHORIZED_MEMBER -> {
                            }
                            else -> {}
                        }
                    }
                }
            }
        }

        binding.settingButton.setOnClickListener {
            val intent = Intent(this@MyPageFragment.requireContext(), MyInfoSettingActivity::class.java)
            startActivity(intent)
        }

        binding.matchingImageButton.setOnClickListener {
            startActivity(Intent(this@MyPageFragment.requireContext(), MyMatchingProfileActivity::class.java))

        }

        binding.dislikedRoomMatesButton.setOnClickListener {
            startActivity(Intent(this@MyPageFragment.requireContext(), DisLikedMateListActivity::class.java))
        }

        binding.likedRoomMatesButton.setOnClickListener {
            startActivity(Intent(this@MyPageFragment.requireContext(), LikedMateListActivity::class.java))
        }
    }
}