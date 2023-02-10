package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.model.ContentUriRequestBody
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.PostEditDto
import org.appcenter.inudorm.usecase.PostUpdateParams
import org.appcenter.inudorm.usecase.UpdatePost
import org.appcenter.inudorm.usecase.WritePost
import java.io.File

data class UploadableImage(val image: Image, var file: File)

data class EditorState(
    val anonymous: Boolean,
    val postId: Int? = null,
    val dormNum: Dorm = Dorm.DORM1,
    val title: String = "",
    val content: String = "",
    val images: ArrayList<UploadableImage> = arrayListOf(),
)

class EditorViewModel : ViewModel() {
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

    fun setImages(images: List<UploadableImage>) {
        _editorState.update {
            it.copy(images = images as ArrayList<UploadableImage>)
        }
    }

    fun writePost() {
        viewModelScope.launch {
            kotlin.runCatching {
                WritePost().run(
                    PostEditDto(
                        _editorState.value.title,
                        _editorState.value.content,
                        _editorState.value.dormNum,
                        _editorState.value.anonymous,
                        _editorState.value.images.map { it.file }
                    )
                )
            }.onSuccess {
                // Todo: 글쓰기 성공.
            }.onFailure {
                // todo: 글쓰기 실패.
            }
        }
    }

    fun editPost() {
        val orgPostId = _editorState.value.postId
        if (orgPostId != null)
            viewModelScope.launch {
                kotlin.runCatching {
                    UpdatePost().run(
                        PostUpdateParams(
                            orgPostId,
                            PostEditDto(
                                _editorState.value.title,
                                _editorState.value.content,
                                _editorState.value.dormNum,
                                _editorState.value.anonymous,
                                _editorState.value.images.map { it.file }
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