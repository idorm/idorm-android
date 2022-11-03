package org.appcenter.inudorm.util

import android.util.Log
import androidx.databinding.*
import com.google.android.material.chip.ChipGroup
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod

object DormFilterBinding {
    @BindingAdapter("selectedDorm")
    @JvmStatic
    fun ChipGroup.setDorm(dorm: Dorm?) {
        Log.i("DormFilterBinding", "$dorm")
        if (dorm != null) {
            check(dorm.elementId)
        }
    }

    @InverseBindingAdapter(attribute = "selectedDorm", event = "android:onCheckedAttrChanged")
    @JvmStatic
    fun ChipGroup.getDorm() : Dorm?{
        return Dorm.fromElementId(checkedChipId)
    }

    @BindingAdapter(value = ["android:onCheckedAttrChanged"], requireAll = false)
    @JvmStatic
    fun ChipGroup.setEventListener(attrChanged: InverseBindingListener?) {
        setOnCheckedChangeListener { _, _ ->
            attrChanged?.onChange()
        }
    }

    @BindingAdapter("selectedJoinPeriod")
    @JvmStatic
    fun ChipGroup.setJoinPeriod(joinPeriod: JoinPeriod?) = joinPeriod?.elementId?.let { check(it) }

    @InverseBindingAdapter(attribute = "selectedJoinPeriod", event = "android:onCheckedAttrChanged")
    @JvmStatic
    fun ChipGroup.getJoinPeriod() : JoinPeriod?{
        return JoinPeriod.fromElementId(checkedChipId)
    }

}