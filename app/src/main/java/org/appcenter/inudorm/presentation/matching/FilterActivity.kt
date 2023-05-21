package org.appcenter.inudorm.presentation.matching

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityFilterBinding
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.RoomMateFilter
import org.appcenter.inudorm.model.Taste
import org.appcenter.inudorm.util.IDormLogger

val defaultFilter =
    RoomMateFilter(
        dormCategory = Dorm.DORM1,
        joinPeriod = JoinPeriod.WEEK16,
        minAge = 20,
        maxAge = 40,
        disAllowedFeatures = mutableListOf()
    )

class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
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

    private fun bind(it: RoomMateFilter) {
        binding.ageRangeSeekbar.setProgress(it.minAge.toFloat(), it.maxAge.toFloat())
        binding.ageFrom.text = checkSeekVal(it.minAge.toFloat())
        binding.ageTo.text = checkSeekVal(it.maxAge.toFloat())

        binding.selectedDorm.apply {
            check(it.dormCategory.elementId)

        }
        binding.joinPeriod.apply {
            check(it.joinPeriod.elementId)
        }

        binding.disallowedFeatures.clearCheck()
        for (disAllowedFeature in it.disAllowedFeatures) {
            binding.disallowedFeatures.check(disAllowedFeature)
        }

    }

    private fun initSeekbar() {
        binding.selectedDorm.setOnCheckedStateChangeListener { view, checkedIds ->
            viewModel.filterState.update {
                it.copy(dormCategory = Dorm.fromElementId(checkedIds[0]) ?: Dorm.DORM1)
            }
        }
        binding.joinPeriod.setOnCheckedStateChangeListener { view, checkedIds ->
            viewModel.filterState.update {
                it.copy(joinPeriod = JoinPeriod.fromElementId(checkedIds[0]) ?: JoinPeriod.WEEK16)
            }
        }
        binding.disallowedFeatures.setOnCheckedStateChangeListener { view, checkedIds ->
            viewModel.filterState.update {
                it.copy(disAllowedFeatures = view.checkedChipIds)
            }
        }

        binding.ageRangeSeekbar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean,
            ) {
                if (isFromUser)
                    viewModel.filterState.update {
                        it.copy(
                            minAge = leftValue.toInt(),
                            maxAge = rightValue.toInt()
                        )
                    }
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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        lifecycleScope.launch {
            viewModel.filterState.collect {
                bind(it)
            }
        }

        viewModel.filterState.update { filter }

        initSeekbar()
        binding.filterDoneButton.setOnClickListener {
            submit(viewModel.filterState.value)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.single_close, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.close -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //seekBar의 val값이 40인지 확인하는 함수
    private fun checkSeekVal(v: Float): String = if (v >= 40) "40+" else v.toInt().toString()
}