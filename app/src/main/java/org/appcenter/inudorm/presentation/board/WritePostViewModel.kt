package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.mypage.UiState
import org.appcenter.inudorm.presentation.mypage.runCatch
import org.appcenter.inudorm.usecase.GetSinglePost

data class ImageFile(val url: String)

data class EditorState(
    val anonymous: Boolean,
    val title: String = "",
    val content: String = "",
    val images: ArrayList<ImageFile> = arrayListOf()
)

class WritePostViewModel : ViewModel() {
    private val _editorState = MutableStateFlow(EditorState(true))
    val editorState: StateFlow<EditorState>
        get() = _editorState

    fun toggleAnonymous() {
        _editorState.update {
            it.copy(anonymous = !it.anonymous)
        }
    }

    fun setTitle(s: CharSequence, start: Int, before: Int, count: Int) {
        _editorState.update {
            it.copy(title = s.toString())
        }
    }

    fun setContent(s: CharSequence, start: Int, before: Int, count: Int) {
        _editorState.update {
            it.copy(content = s.toString())
        }
    }

}