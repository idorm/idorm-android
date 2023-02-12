package org.appcenter.inudorm.util.bindingadapter

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.appcenter.inudorm.R
import org.appcenter.inudorm.util.IDormLogger

object ImageBinding {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun ImageView.setCachedNetworkImage(imageUrl: String?) {
        IDormLogger.i(this, imageUrl.toString())
        if (imageUrl?.isNotEmpty() == true)
            Glide.with(this).load(imageUrl).into(this)
        else setImageDrawable(
            ContextCompat.getDrawable(
                this.context,
                R.drawable.ic_profile_my_page
            )
        )
    }

}