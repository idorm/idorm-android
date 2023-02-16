package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.Post
import java.io.File

data class UploadableImage(val image: Image, var file: File)

data class EditorState(
    val anonymous: Boolean,
    val postId: Int? = null,
    val dormCategory: Dorm = Dorm.DORM1,
    val images: ArrayList<UploadableImage> = arrayListOf(),
)

open class EditorViewModel : ViewModel() {
    private val _editorState = MutableStateFlow(EditorState(true))
    val editorState: StateFlow<EditorState>
        get() = _editorState

    val title = MutableLiveData("")
    val content = MutableLiveData("")

    fun setInitialPost(post: Post) {
        title.value = post.title
        content.value = post.content
        _editorState.update {
            it.copy(
                postId = post.postId,
                dormCategory = post.dormCategory,
                anonymous = post.isAnonymous()
            )
        }
    }


    fun setDorm(dorm: String?) {
        _editorState.update {
            it.copy(
                dormCategory = Dorm.values().find { it.name == dorm }
                    ?: throw java.lang.RuntimeException("올바르지 않은 기숙사.")
            )
        }
    }

    fun toggleAnonymous() {
        _editorState.update {
            it.copy(anonymous = !it.anonymous)
        }
    }

    fun setImages(images: List<UploadableImage>) {
        _editorState.update {
            it.copy(images = images as ArrayList<UploadableImage>)
        }
    }


}