package org.appcenter.inudorm.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityFilterBinding

class FilterActivity : AppCompatActivity() {

    private  val TAG = "[FilterActivity]"

    private lateinit var binding: ActivityFilterBinding
    private val filterViewModel: FilterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        binding.filterViewModel = filterViewModel

        val ageDualSeekbar = binding.ageDualSeekbar
        val ageValLeft = binding.ageValLeft
        val ageValRight = binding.ageValRight


        ageDualSeekbar.setProgress(20f, 40f)
        ageValLeft.text = ageDualSeekbar.leftSeekBar.toString()
        ageValRight.text= ageDualSeekbar.progressRight.toString()

        ageDualSeekbar.setOnRangeChangedListener(object: OnRangeChangedListener{
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                val padding = ageDualSeekbar.leftSeekBar.indicatorPaddingLeft
                Log.d(TAG, "onRangeChanged: $padding")

                ageValLeft.text= leftValue.toInt().toString()
                ageValRight.text= rightValue.toInt().toString()


            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })


    }
}