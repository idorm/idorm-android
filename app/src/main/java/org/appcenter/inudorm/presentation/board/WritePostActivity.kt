package org.appcenter.inudorm.presentation.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWritePostBinding
import org.appcenter.inudorm.util.IDormLogger

class WritePostActivity : AppCompatActivity() {

    private val binding: ActivityWritePostBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_write_post)
    }

    private val viewModel: WritePostViewModel by viewModels()

    private val launcher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            val sampleImage = images[0]
            IDormLogger.i(this, sampleImage.name)
/*
            Glide.with(this@WritePostActivity)
                .load(sampleImage.uri)
                .into(binding.image)
*/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this
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

            isMultipleMode = true,
            doneTitle = "완료",
            maxSize = 4,
            isShowNumberIndicator = true
        )

        launcher.launch(config)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.board_write_menu, menu)
        return true
    }
}