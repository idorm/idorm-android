package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.usecase.PostUpdateParams
import org.appcenter.inudorm.usecase.UpdatePost
import java.io.File

class EditPostViewModel : EditorViewModel() {

    private val _editPostResult = MutableStateFlow<State?>(null)
    val editPostResult: StateFlow<State?>
        get() = _editPostResult

    fun editPost() {
        val orgPostId = editorState.value.postId
        if (orgPostId != null)
            viewModelScope.launch {
                kotlin.runCatching {
                    UpdatePost().run(
                        PostUpdateParams(
                            orgPostId,
                            PostEditDto(
                                title.value!!,
                                content.value!!,
                                editorState.value.dormCategory,
                                editorState.value.anonymous,
                                editorState.value.images
                                    .filter { it.file != null }
                                    .map { it.file!! },
                            )
                        )
                    )
                }.onSuccess {
                    // Todo: 글쓰기 성공.
                    _editPostResult.emit(State.Success(it))
                }.onFailure {
                    // todo: 글쓰기 실패.
                    _editPostResult.emit(State.Error(it))
                }
            }
    }
}