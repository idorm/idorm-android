package org.appcenter.inudorm.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import org.appcenter.inudorm.R

enum class ButtonType {
    Flat,
    Filled
}

data class DialogButton(
    val text: String,
    val buttonType: ButtonType = ButtonType.Flat,
    @DrawableRes
    val icon: Int? = null,
    val onClick: (() -> Unit)? = null,
    @ColorRes val textColor: Int = R.color.iDorm_blue,
)

// Todo: Dialog 코드 개똥방구임 확장성 개똥..; 나중에 꼭 리팩토링 하자;
open class CustomDialog(
    private val text: String,
    private val titleText: String? = "",
    private val positiveButton: DialogButton? = null,
    private val negativeButton: DialogButton? = null,
) {

    fun show(context: Context) {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.layout_dialog, null)
        val build = AlertDialog.Builder(context).apply {
            setView(customLayout)
        }

        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val title = customLayout.findViewById<TextView>(R.id.title)
        val closeButton = customLayout.findViewById<ImageView>(R.id.close)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        val textView = customLayout.findViewById<TextView>(R.id.text)
        textView.text = text

        if (positiveButton?.buttonType == ButtonType.Filled) {
            textView.gravity = Gravity.CENTER_HORIZONTAL
            title.text = titleText
            title.visibility = View.VISIBLE
            closeButton.visibility = View.VISIBLE
        }

        if (positiveButton != null) {
            val btnOk = customLayout.findViewById<LinearLayout>(R.id.ok_button)
            btnOk.apply {
                visibility = View.VISIBLE
                val btnText = customLayout.findViewById<TextView>(R.id.btnText)
                val btnIcon = customLayout.findViewById<ImageView>(R.id.btnIcon)
                btnText.text = positiveButton.text
                btnText.setTextColor(
                    ContextCompat.getColor(
                        getContext(),
                        if (positiveButton.buttonType == ButtonType.Filled && positiveButton.textColor == R.color.iDorm_blue) R.color.white else R.color.iDorm_blue
                    )
                )
                if (positiveButton.icon != null) btnIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this.context,
                        positiveButton.icon
                    )
                )
                setOnClickListener {
                    positiveButton.onClick?.invoke()
                    dialog.dismiss()
                }
                //setBackgroundResource(R.drawable.view_button_round_28)
                if (positiveButton.buttonType == ButtonType.Filled) {
                    IDormLogger.i(this@CustomDialog, "filled button!")
                    this.background =
                        ContextCompat.getDrawable(this.context, R.drawable.view_button_round_10)
                    this.layoutParams.width = LayoutParams.MATCH_PARENT
                    this.gravity = Gravity.CENTER_HORIZONTAL
                    this.setPadding(16)

                }
            }
        }

        if (negativeButton != null) {
            val btnCancel = customLayout.findViewById<TextView>(R.id.cancel_button)
            btnCancel.apply {
                visibility = View.VISIBLE
                text = negativeButton.text
                setTextColor(ContextCompat.getColor(getContext(), negativeButton.textColor))
                setOnClickListener {
                    negativeButton.onClick?.invoke()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}