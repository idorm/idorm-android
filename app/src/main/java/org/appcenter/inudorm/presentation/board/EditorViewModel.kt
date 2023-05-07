package org.appcenter.inudorm.presentation.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.Photo
import org.appcenter.inudorm.model.board.Post
import java.io.File

data class EditorState(
    val anonymous: Boolean,
    val postId: Int? = null,
    val dormCategory: Dorm = Dorm.DORM1,
    val images: ArrayList<Photo> = arrayListOf(),
    val deletedPhotoIds: ArrayList<Int>? = null,
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
                anonymous = post.isAnonymous,
                images = post.postPhotos ?: arrayListOf(),
            )
        }
    }


    fun deleteImage(idx: Int) {
        _editorState.update {
            val newImages = ArrayList(it.images)
            val newDeletedPhotoIds = ArrayList(it.deletedPhotoIds ?: arrayListOf())
            newImages.removeAt(idx)
            // image는 로컬에서 pick 한 이미지만 갖고 있는 값. => 기존에 있던 이미지는 image == null
            if (it.images[idx].image == null && it.images[idx].photoId != null) {
                newDeletedPhotoIds.add(it.images[idx].photoId!!)
            }
            it.copy(
                images = newImages,
                deletedPhotoIds = newDeletedPhotoIds
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

    fun setImages(images: List<Photo>) {
        _editorState.update {
            it.copy(images = images as ArrayList<Photo>)
        }
    }

    fun addImages(images: List<Photo>) {
        _editorState.update {
            val newImages = ArrayList(it.images)
            newImages.addAll(images as ArrayList<Photo>)
            it.copy(images = newImages)
        }
    }

}