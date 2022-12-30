package org.appcenter.inudorm.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityFilterBinding
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.model.Taste
import org.appcenter.inudorm.util.IDormLogger

val defaultFilter =
    RoomMateFilter(
        dormNum = Dorm.DORM1,
        joinPeriod = JoinPeriod.WEEK16,
        minAge = 20,
        maxAge = 40,
        disAllowedFeatures = mutableListOf()
    )

class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding

    /*override fun onBackPressed() {
        submit(viewModel.filterState.value)
        super.onBackPressed()
    }*/

    private fun submit(filter: RoomMateFilter) {
        // disallowedFeatures를 변경.

        Taste.values().forEach {
            val field =
                RoomMateFilter::class.java.declaredFields.find { field -> field.name == it.key }
            field?.isAccessible = true
            field?.set(filter, filter.disAllowedFeatures.contains(it.elementId))
        }
        val intent = Intent(applicationContext, MatchingFragment::class.java)
            .putExtra("filter", filter)
        setResult(FILTER_RESULT_CODE, intent)
        finish()
    }

    private fun initSeekbar() {
        val ageRangeSeekbar = binding.ageRangeSeekbar
        ageRangeSeekbar.setProgress(20f, 40f)
        var valueFrom = ageRangeSeekbar.leftSeekBar.progress
        var valueTo = ageRangeSeekbar.rightSeekBar.progress

        val ageFrom = binding.ageFrom
        val ageTo = binding.ageTo


        ageFrom.text = checkSeekVal(valueFrom)
        ageTo.text = checkSeekVal(valueTo)

        ageRangeSeekbar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {

                ageFrom.text = checkSeekVal(leftValue)
                ageTo.text = checkSeekVal(rightValue)

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

    private lateinit var viewModel: FilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = intent?.getParcelableExtra("filter") ?: defaultFilter
        IDormLogger.i(this, filter.toString())
        Taste.values().forEach {
            RoomMateFilter::class.java.declaredFields.forEach { field ->
                if (field.name == it.key) {
                    field.isAccessible = true
                    if (field.getBoolean(filter))
                        filter.disAllowedFeatures.add(
                            it.elementId
                        )
                }
            }
        }

        viewModel = ViewModelProvider(
            viewModelStore,
            FilterViewModelFactory(filter)
        )[FilterViewModel::class.java]

        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)

        binding.apply {
            filterViewModel = viewModel
            lifecycleOwner = this@FilterActivity
        }
        initSeekbar()
        binding.filterDoneButton.setOnClickListener {
            submit(viewModel.filterState.value)
        }
    }

    //seekBar의 val값이 40인지 확인하는 함수
    private fun checkSeekVal(v: Float): String = if (v >= 40) "40+" else v.toInt().toString()
}