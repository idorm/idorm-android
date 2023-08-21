package org.appcenter.inudorm.presentation.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.appcenter.inudorm.LoadingActivity
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ActivityWriteSleepoverScheduleBinding

class WriteSleepoverScheduleActivity : LoadingActivity() {

    private val binding : ActivityWriteSleepoverScheduleBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_write_sleepover_schedule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_sleepover_schedule)
    }
}