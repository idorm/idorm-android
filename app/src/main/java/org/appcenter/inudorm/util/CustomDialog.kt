package org.appcenter.inudorm.util

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.transition.Visibility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import org.appcenter.inudorm.R

data class DialogButton(
    val text: String,
    val onClick: (() -> Unit)? = null,
    @ColorRes val textColor: Int = R.color.iDorm_blue,
)


open class CustomDialog(
    private val text: String, private val positiveButton: DialogButton? = null, private val negativeButton: DialogButton? = null
) {
    fun show(context: Context) {

        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.layout_dialog, null)
        val build = AlertDialog.Builder(context).apply {
            setView(customLayout)
        }

        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textView = customLayout.findViewById<TextView>(R.id.text)
        textView.text = text

        if (positiveButton != null) {
            val btnOk = customLayout.findViewById<TextView>(R.id.ok_button)
            btnOk.apply {
                visibility = View.VISIBLE
                text = positiveButton.text
                setTextColor(ContextCompat.getColor(getContext(),positiveButton.textColor))
                setOnClickListener {
                    positiveButton.onClick?.invoke()
                    dialog.dismiss()
                }
            }
        }

        if (negativeButton != null) {
            val btnCancel = customLayout.findViewById<TextView>(R.id.cancel_button)
            btnCancel.apply {
                visibility = View.VISIBLE
                text = negativeButton.text
                setTextColor(ContextCompat.getColor(getContext(),negativeButton.textColor))
                setOnClickListener {
                    negativeButton.onClick?.invoke()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}