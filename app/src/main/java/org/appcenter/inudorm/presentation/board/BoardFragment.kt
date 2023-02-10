package org.appcenter.inudorm.presentation.board

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.solver.state.State
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yuyakaido.android.cardstackview.Direction
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.FragmentBoardBinding
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.presentation.ListBottomSheet
import org.appcenter.inudorm.presentation.adapter.PopularPostAdapter
import org.appcenter.inudorm.presentation.adapter.PostAdapter
import org.appcenter.inudorm.usecase.BoardType
import org.appcenter.inudorm.util.WindowUtil.setStatusBarColor

class BoardFragment : Fragment() {

    companion object {
        fun newInstance() = BoardFragment()
    }

    private lateinit var viewModel: BoardViewModel
    private lateinit var binding: FragmentBoardBinding

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
        startActivity(intent)
    }

    fun write() {
        startActivity(Intent(requireContext(), EditorActivity::class.java))
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
        viewModel = ViewModelProvider(this)[BoardViewModel::class.java]
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
        when (item.itemId) {
            R.id.openNoti -> {
                Toast.makeText(requireContext(), "알림 창 열기", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}