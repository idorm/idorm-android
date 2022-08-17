package org.appcenter.inudorm.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity

class LinedEditText(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {
    private val mRect: Rect = Rect()
    private val mPaint: Paint = Paint()


    override fun onDraw(canvas: Canvas) {
        // View 크기를 줄간격으로 나눠 총 줄 수를 구합니다.
        var count = height / lineHeight

        // 입력된 텍스트의 줄 수가 계산된 줄 수보다 크면 계산된 줄 수를 텍스트의 줄 수로 대입합니다.
        if (lineCount > count) count = lineCount

        // 줄 그리기..
        val r = mRect
        val paint = mPaint
        var baseline = getLineBounds(0, r)

        for (i in 0 until count) {
            canvas.drawLine(
                r.left.toFloat(), (baseline+15).toFloat(),
                r.right.toFloat(), (baseline+15).toFloat(), paint
            )
            baseline += lineHeight
        }
        super.onDraw(canvas)
    }

    // we need this constructor for LayoutInflater
    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.parseColor("#000000")
        gravity = Gravity.TOP
        addTextChangedListener(object : TextWatcher {
            private var prevString = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                prevString = s.toString()

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (lineCount > 8) {
                    setText(prevString)
                    setSelection(length())
                }
            }

        })
    }
}