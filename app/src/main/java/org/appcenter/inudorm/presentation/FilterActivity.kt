package org.appcenter.inudorm.presentation

import android.content.Intent
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
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.RoomMateFilter

class FilterActivity : AppCompatActivity() {

    private  val TAG = "[FilterActivity]"

    private lateinit var binding: ActivityFilterBinding
    private val filterViewModel: FilterViewModel by viewModels()

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MatchingFragment::class.java)
        intent.putExtra("filter", RoomMateFilter(dormNum = Dorm.Third, minAge = 25, maxAge = 26, joinPeriod = JoinPeriod.Long))
        setResult(FILTER_RESULT_CODE, intent)
        finish()
        super.onBackPressed()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        binding.filterViewModel = filterViewModel

        val filter = intent?.getParcelableExtra<RoomMateFilter>("filter")
        Log.i(TAG, filter.toString())

        val ageDualSeekbar = binding.ageDualSeekbar
        var ageValLeft = binding.ageValLeft
        val ageValRight = binding.ageValRight

        ageDualSeekbar.setProgress(20f, 40f)

        var nowLeftval = ageDualSeekbar.leftSeekBar.progress
        var nowRigthtval = ageDualSeekbar.rightSeekBar.progress

        ageValLeft.text = checkSeekVal(nowLeftval)
        ageValRight.text = checkSeekVal(nowRigthtval)




        ageDualSeekbar.setOnRangeChangedListener(object: OnRangeChangedListener{
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {

                ageValLeft.text= checkSeekVal(leftValue)
                ageValRight.text = checkSeekVal(rightValue)

                //현재 seekBar 퍼센트 계산
                var leftPercent = (leftValue.toInt() - 20) * 5 * 0.01
                var rightPercent = (rightValue.toInt() - 20) * 5 * 0.01

                /*
                var leftPos = ageDualSeekbar.width * leftPercent
                var rigthPos = ageDualSeekbar.width * rightPercent
                
                ageValLeft?.x = leftPos.toInt().toFloat()
                ageValRight?.x = rigthPos.toInt().toFloat()

                var x =ageDualSeekbar.x
                Log.d(TAG, "onRangeChanged: $rigthPos, $rightPercent")
                */

            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })


    }

    //seekBar의 val값이 40인지 확인하는 함수
    private fun checkSeekVal(v : Float) : String {
        var seekbarVal = ""
        if(v >=40) seekbarVal = "40+"
        else seekbarVal = v.toInt().toString()
        return seekbarVal
    }

}