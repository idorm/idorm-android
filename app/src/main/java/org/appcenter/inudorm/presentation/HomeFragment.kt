package org.appcenter.inudorm.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentHomeBinding
import org.appcenter.inudorm.model.Schedule
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.networking.UIErrorHandler
import org.appcenter.inudorm.presentation.adapter.CalendarAdapter
import org.appcenter.inudorm.presentation.adapter.PopularPostAdapter
import org.appcenter.inudorm.presentation.board.BoardFragment
import org.appcenter.inudorm.presentation.board.PostDetailActivity
import org.appcenter.inudorm.presentation.board.WritePostActivity
import org.appcenter.inudorm.presentation.matching.MatchingFragment
import org.appcenter.inudorm.repository.PrefsRepository
import org.appcenter.inudorm.util.State
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor

class HomeFragment : LoadingFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    private val prefsRepository by lazy {
        PrefsRepository(this.requireContext())
    }

    private val topPostsCollector = FlowCollector<State<ArrayList<Post>>> { state ->
        setLoadingState(state.isLoading())
        when (state) {
            is State.Error -> {
                // 공통 에러 처리만 할게요.
                UIErrorHandler.handle(
                    this@HomeFragment.requireContext(),
                    prefsRepository = prefsRepository,
                    state.error
                )
            }
            else -> {}
        }
    }
    private val scheduleCollector = FlowCollector<State<ArrayList<Schedule>>> { state ->
        setLoadingState(state.isLoading())
        when (state) {
            is State.Error -> {
                // 공통 에러 처리만 할게요.
                UIErrorHandler.handle(
                    this@HomeFragment.requireContext(),
                    prefsRepository = prefsRepository,
                    state.error
                )
            }
            else -> {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            WritePostActivity.EDITOR_FINISHED, PostDetailActivity.DETAIL_FINISHED -> {
                viewModel.getTopPosts()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.popularPosts.adapter = PopularPostAdapter(ArrayList()) {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra("id", it.postId)
            startActivityForResult(intent, BoardFragment.DETAIL_OPEN)
        }
        binding.officialEvents.adapter = CalendarAdapter(arrayListOf()) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
        }

        viewModel.getTopPosts()
        viewModel.getCalendars()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.requireActivity()
        binding.startMatching.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(R.id.container, MatchingFragment())
                .remove(this)
                .commit()
        }

        lifecycleScope.launch {
            viewModel.topPostState.collect(topPostsCollector)
        }
        lifecycleScope.launch {
            viewModel.scheduleState.collect(scheduleCollector)
        }
    }

}