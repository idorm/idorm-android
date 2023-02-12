package org.appcenter.inudorm.presentation.board

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityEditorBinding
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.adapter.ImageViewAdapter
import org.appcenter.inudorm.util.IDormLogger
import org.appcenter.inudorm.util.ImageUri
import java.io.File

abstract class EditorActivity : LoadingActivity() {

    val binding: ActivityEditorBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_editor)
    }

    abstract val viewModel: EditorViewModel
    private var imageViewAdapter: ImageViewAdapter? = null


    private val launcher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            try {
                viewModel.setImages(
                    images.map {
                        UploadableImage(
                            it,
                            File(
                                ImageUri.getFullPathFromUri(this@EditorActivity, it.uri)
                                    ?: throw RuntimeException()
                            )
                        )
                    }
                )
            } catch (e: RuntimeException) {
                e.printStackTrace()
                Toast.makeText(this@EditorActivity, "이미지를 불러오지 못헀습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }
    var orgPost: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        orgPost = intent.getParcelableExtra<Post>("post")
        if (orgPost != null) {
            viewModel.title.value = orgPost!!.title
            viewModel.content.value = orgPost!!.content
            val images = ArrayList<Image>()
            images.addAll((orgPost!!.photoUrls?.map {
                Image(Uri.parse(it), "", 0, "")
            })!!)
        }

        if (imageViewAdapter == null) {
            imageViewAdapter = ImageViewAdapter(arrayListOf()) {
                // Todo: Show fullsize image
            }
            binding.imageView.adapter = imageViewAdapter
        }
    }


    fun openImagePicker() {
        // Todo: 수정 모드에서 이미 선택된 사진  불러오기
        val config = ImagePickerConfig(
            statusBarColor = "#ffffff",
            isLightStatusBar = true,
            toolbarColor = "#ffffff",
            toolbarTextColor = "#000000",
            toolbarIconColor = "#000000",
            imageTitle = "모든 사진",
            backgroundColor = "#ffffff",
            selectedImages = viewModel.editorState.value.images.map { it.image } as java.util.ArrayList<Image>,
            isMultipleMode = true,
            doneTitle = "완료",
            maxSize = 10,
            isShowNumberIndicator = true
        )

        launcher.launch(config)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.board_write_menu, menu)
        return true
    }

}