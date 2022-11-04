package org.appcenter.inudorm.util

import android.util.Log
import androidx.databinding.*
import com.google.android.material.chip.ChipGroup
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod

object DormFilterBinding {
    @JvmStatic
    @BindingAdapter("selectedDorm")
    fun ChipGroup.setDorm(dorm: Dorm?) {
        Log.i("DormFilterBinding", "$dorm")
        if (dorm != null) {
            check(dorm.elementId)
        }
    }
    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedDorm", event = "android:onCheckedAttrChanged")
    fun ChipGroup.getDorm() : Dorm?{
        return Dorm.fromElementId(checkedChipId)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:onCheckedAttrChanged"], requireAll = false)
    fun ChipGroup.setEventListener(attrChanged: InverseBindingListener?) {
        setOnCheckedChangeListener { _, _ ->
            attrChanged?.onChange()
        }
    }

    @JvmStatic
    @BindingAdapter("selectedJoinPeriod")
    fun ChipGroup.setJoinPeriod(joinPeriod: JoinPeriod?) = joinPeriod?.elementId?.let { check(it) }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedJoinPeriod", event = "android:onCheckedAttrChanged")
    fun ChipGroup.getJoinPeriod() : JoinPeriod?{
        return JoinPeriod.fromElementId(checkedChipId)
    }

}