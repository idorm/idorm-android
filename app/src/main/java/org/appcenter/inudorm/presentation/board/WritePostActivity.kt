package org.appcenter.inudorm.presentation.board

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton

const val EDITOR_FINISHED = 4734

class WritePostActivity : EditorActivity() {
    override val viewModel: WritePostViewModel by viewModels()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.doneButton -> {
                viewModel.writePost()
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.writePostResult.collect {
                when (it) {
                    is State.Success<*> -> {
                        this@WritePostActivity.setLoadingState(false)
                        CustomDialog(
                            "게시글이 등록되었습니다.",
                            positiveButton = DialogButton(
                                "확인",
                                onClick = { finishActivity(EDITOR_FINISHED) })
                        ).show(this@WritePostActivity)
                    }
                    is State.Error -> {
                        this@WritePostActivity.setLoadingState(false)
                        CustomDialog(
                            "게시글 작성에 실패했습니다.",
                            titleText = "오류",
                            positiveButton = DialogButton(
                                "확인",
                                onClick = { finishActivity(EDITOR_FINISHED) })
                        ).show(this@WritePostActivity)
                    }
                    is State.Loading -> {
                        this@WritePostActivity.setLoadingState(true)
                    }
                    else -> {
                        this@WritePostActivity.setLoadingState(false)

                    }
                }
            }
        }

    }

}