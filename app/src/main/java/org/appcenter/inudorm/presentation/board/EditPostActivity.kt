package org.appcenter.inudorm.presentation.board

import android.view.MenuItem
import androidx.activity.viewModels
import org.appcenter.inudorm.R

class EditPostActivity : EditorActivity() {
    override val viewModel: EditPostViewModel by viewModels()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.doneButton -> {
                viewModel.editPost()
            }
        }
        return true
    }

}