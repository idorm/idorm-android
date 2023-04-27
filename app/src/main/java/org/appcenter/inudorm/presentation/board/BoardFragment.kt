package org.appcenter.inudorm.presentation.board

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentBoardBinding
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.PopularPostAdapter
import org.appcenter.inudorm.presentation.adapter.PostAdapter
import org.appcenter.inudorm.presentation.board.PostDetailActivity.Companion.DETAIL_FINISHED
import org.appcenter.inudorm.presentation.board.WritePostActivity.Companion.EDITOR_FINISHED
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor

class BoardFragment : Fragment() {

    companion object {
        fun newInstance() = BoardFragment()
        const val WRITER_OPEN = 7659
        const val DETAIL_OPEN = 7660
    }

    private lateinit var viewModel: BoardViewModel
    private lateinit var binding: FragmentBoardBinding

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            EDITOR_FINISHED, DETAIL_FINISHED -> {
                viewModel.getAllPosts()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        return binding.root
    }

    val dorms = Dorm.values().map { SelectItem("제${it.text}기숙사", it.name) } as ArrayList<SelectItem>

    fun goDetail(id: Int) {
        val intent = Intent(requireContext(), PostDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivityForResult(intent, DETAIL_OPEN)
    }

    fun write() {
        val intent = Intent(requireContext(), WritePostActivity::class.java)
        intent.putExtra("dormCategory", viewModel.boardUiState.value.selectedDorm.value)
        startActivityForResult(intent, WRITER_OPEN)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().setStatusBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BoardViewModel::class.java)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = requireActivity()

        binding.popularPosts.adapter = PopularPostAdapter(ArrayList()) {
            goDetail(it.postId)
        }

        binding.posts.adapter = PostAdapter(ArrayList()) {
            goDetail(it.postId)
        }
        binding.posts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.addPages()
                }
            }
        })
        binding.refreshLayout.setOnRefreshListener {
            viewModel.setPage(0)
            viewModel.getAllPosts()
        }

        viewModel.getAllPosts()

        // TODO: Use the ViewModel
    }

    fun openSelector() {
        ListBottomSheet(dorms) {
            viewModel.setDorm(it)
        }.show(
            parentFragmentManager,
            ListBottomSheet.TAG
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        return super.onOptionsItemSelected(item)
    }

}