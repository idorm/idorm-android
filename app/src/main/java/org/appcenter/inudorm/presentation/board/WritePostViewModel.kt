package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.usecase.WritePost

sealed class State {
    data class Success<T>(val data: T) : State()
    object Loading : State()
    data class Error(val error: Throwable) : State()
}

class WritePostViewModel : EditorViewModel() {
    private val _writePostResult = MutableStateFlow<State?>(null)
    val writePostResult: StateFlow<State?>
        get() = _writePostResult


    fun writePost() {
        viewModelScope.launch {
            kotlin.runCatching {
                WritePost().run(
                    PostEditDto(
                        title.value!!,
                        content.value!!,
                        editorState.value.dormCategory,
                        editorState.value.anonymous,
                        editorState.value.images
                            .filter { it.file != null }
                            .map { it.file!! }
                    )
                )
            }.onSuccess {
                // Todo: 글쓰기 성공.
                _writePostResult.emit(State.Success(it))
            }.onFailure {
                // todo: 글쓰기 실패.
                _writePostResult.emit(State.Error(it))
            }
        }
    }

}