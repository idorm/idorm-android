package org.appcenter.inudorm.model.board

import android.os.Parcelable
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Photo(
    val image: Image? = null,
    var file: File? = null,
    var photoId: Int? = null,
    var photoUrl: String = image?.uri.toString(),
) : Parcelable