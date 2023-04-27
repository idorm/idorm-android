package org.appcenter.inudorm.presentation.board

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import org.appcenter.inudorm.R
import org.appcenter.inudorm.util.CustomDialog
import org.appcenter.inudorm.util.DialogButton
import org.appcenter.inudorm.util.OkDialog


class WritePostActivity : EditorActivity() {
    override val viewModel: WritePostViewModel by viewModels()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(EDITOR_FINISHED)
                finish()
            }
            R.id.doneButton -> {
                viewModel.writePost()
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dorm = intent.getStringExtra("dormCategory")
        viewModel.setDorm(dorm)

        lifecycleScope.launchWhenCreated {
            viewModel.writePostResult.collect {
                when (it) {
                    is State.Success<*> -> {
                        this@WritePostActivity.setLoadingState(false)
                        OkDialog("게시글이 등록되었습니다.", onOk = {
                            setResult(EDITOR_FINISHED)
                            finish()
                        }, cancelable = false).show(this@WritePostActivity)
                    }
                    is State.Error -> {
                        this@WritePostActivity.setLoadingState(false)
                        OkDialog("게시글 작성에 실패했습니다..", "오류", {
                            finish()
                        }).show(this@WritePostActivity)
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

    companion object {
        const val EDITOR_FINISHED = 4734
    }

}