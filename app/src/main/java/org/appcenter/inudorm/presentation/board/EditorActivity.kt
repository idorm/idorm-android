package org.appcenter.inudorm.presentation.board

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityEditorBinding
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.presentation.adapter.ImageViewAdapter
import javax.inject.Inject


class EditorActivity : AppCompatActivity() {

    private val binding: ActivityEditorBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_editor)
    }

    @Inject
    lateinit var viewModel: EditorViewModel
    private var imageViewAdapter: ImageViewAdapter? = null

    private val launcher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            viewModel.setImages(images)
        }
    }
    private var orgPost: Post? = null

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
            viewModel.setTitle(orgPost!!.title, 0, 0, 0)
            viewModel.setContent(orgPost!!.content, 0, 0, 0)
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
            selectedImages = viewModel.editorState.value.images,
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.doneButton -> {
                if (orgPost == null)
                    viewModel.writePost()
                else
                    viewModel.editPost()
            }
        }
        return true
    }

}