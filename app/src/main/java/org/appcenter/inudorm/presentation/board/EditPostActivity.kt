package org.appcenter.inudorm.presentation.board

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import org.appcenter.inudorm.R
import org.appcenter.inudorm.util.OkDialog

class EditPostActivity : EditorActivity() {
    override val viewModel: EditPostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.editPostResult.collect {
                when (it) {
                    is State.Success<*> -> {
                        this@EditPostActivity.setLoadingState(false)
                        OkDialog("게시글이 수정되었어요.", onOk = {
                            setResult(WritePostActivity.EDITOR_FINISHED)
                            finish()
                        }, cancelable = false).show(this@EditPostActivity)
                    }
                    is State.Error -> {
                        this@EditPostActivity.setLoadingState(false)
                        OkDialog("게시글 수정에 실패했어요..", "오류", onOk = {
                            finish()
                        }).show(this@EditPostActivity)
                    }
                    is State.Loading -> {
                        this@EditPostActivity.setLoadingState(true)
                    }
                    else -> {
                        this@EditPostActivity.setLoadingState(false)

                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(WritePostActivity.EDITOR_FINISHED)
                finish()
            }
            R.id.doneButton -> {
                if (viewModel.title.value.isNullOrEmpty() || viewModel.content.value.isNullOrEmpty())
                    OkDialog("제목과 내용을 모두 입력해주세요.").show(this)
                else
                    viewModel.editPost()
            }
        }
        return true
    }

}