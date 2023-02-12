package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.SelectItem
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.matching.LoadMode
import org.appcenter.inudorm.usecase.BoardType
import org.appcenter.inudorm.usecase.GetPostParams
import org.appcenter.inudorm.usecase.GetPosts

data class BoardUiState(
    val selectedDorm: SelectItem,
    val posts: InfinityScrollState<ArrayList<Post>>,
    val topPosts: InfinityScrollState<ArrayList<Post>>,
    val page: Int = 0,
)

data class InfinityScrollState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: Throwable? = null,
    val loadMode: LoadMode = LoadMode.Prepare,
) {
    companion object {
        inline fun <reified T> empty() =
            InfinityScrollState<T>(
                data = null,
                loading = true,
                error = null,
                loadMode = LoadMode.Prepare
            )

        inline fun <reified T> loading() =
            InfinityScrollState<T>(
                data = null,
                loading = true,
                error = null,
                loadMode = LoadMode.Prepare
            )

        inline fun <reified T> data(data: T, loadMode: LoadMode) =
            InfinityScrollState(
                data = data,
                loading = false,
                error = null,
                loadMode = loadMode
            )

        inline fun <reified T> error(error: Throwable?, loadMode: LoadMode) =
            InfinityScrollState<T>(
                data = null,
                loading = false,
                error = error,
                loadMode = loadMode
            )
    }
}

class BoardViewModel : ViewModel() {
    private val _boardUiState =
        MutableStateFlow(BoardUiState(
            SelectItem("제${Dorm.DORM1.text}기숙사", Dorm.DORM1.name),
            InfinityScrollState.empty(),
            InfinityScrollState.empty()
        ))
    val boardUiState: StateFlow<BoardUiState>
        get() = _boardUiState

    fun getDorm() =
        Dorm.values().find { _boardUiState.value.selectedDorm.value == it.name } ?: Dorm.DORM1

    fun getAllPosts() {
        viewModelScope.launch {
            _boardUiState.update {
                it.copy(
                    posts = InfinityScrollState.loading(),
                    topPosts = InfinityScrollState.loading()
                )
            }
            kotlin.runCatching {
                val posts =
                    GetPosts().run(GetPostParams(BoardType.Regular,
                        getDorm(),
                        _boardUiState.value.page))
                val topPosts =
                    GetPosts().run(GetPostParams(BoardType.Top,
                        getDorm(),
                        _boardUiState.value.page))
                boardUiState.value.copy(
                    posts = InfinityScrollState.data(posts, LoadMode.Update),
                    topPosts = InfinityScrollState.data(topPosts, LoadMode.Update)
                )
            }.onSuccess { data ->
                _boardUiState.update {
                    data
                }
            }.onFailure { err ->
                _boardUiState.update {
                    it.copy(
                        posts = InfinityScrollState.error(err, LoadMode.Update),
                        topPosts = InfinityScrollState.error(err, LoadMode.Update)
                    )
                }
            }
        }
    }

    fun setPage(page: Int) {
        _boardUiState.update {
            it.copy(page = page)
        }
    }

    fun addPages() {
        setPage(_boardUiState.value.page + 1)
        addPosts(BoardType.Regular)
    }

    fun addPosts(boardType: BoardType) {
        viewModelScope.launch {
            _boardUiState.update {
                it.copy(
                    posts = InfinityScrollState.loading()
                )
            }
            kotlin.runCatching {
                GetPosts().run(GetPostParams(boardType, getDorm(), _boardUiState.value.page))
            }.onSuccess { data ->
                _boardUiState.update {
                    it.copy(
                        posts = InfinityScrollState.data(data, LoadMode.Paging)
                    )
                }
            }.onFailure { err ->
                _boardUiState.update {
                    it.copy(
                        posts = InfinityScrollState.error(err, LoadMode.Paging)
                    )
                }
            }
        }
    }

    fun setDorm(dorm: SelectItem) {
        _boardUiState.update {
            it.copy(
                selectedDorm = dorm,
                page = 0
            )
        }
        getAllPosts()
    }
}
