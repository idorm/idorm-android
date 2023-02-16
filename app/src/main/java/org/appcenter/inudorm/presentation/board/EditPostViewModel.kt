package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.usecase.PostUpdateParams
import org.appcenter.inudorm.usecase.UpdatePost

class EditPostViewModel : EditorViewModel() {

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
                                editorState.value.images.map { it.file }
                            )
                        )
                    )
                }.onSuccess {
                    // Todo: 글쓰기 성공.
                }.onFailure {
                    // todo: 글쓰기 실패.
                }
            }
    }
}