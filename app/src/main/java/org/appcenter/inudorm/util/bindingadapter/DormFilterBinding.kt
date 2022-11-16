package org.appcenter.inudorm.util.bindingadapter

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.databinding.*
import com.google.android.material.chip.ChipGroup
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.JoinPeriod
import org.appcenter.inudorm.model.Taste
import org.appcenter.inudorm.util.IDormLogger

/**
 * 특정 도메인에 종속되는 친구들은 따로 빼줘야합니다.
 */
object DormFilterBinding {

    /**
     * ChipGroup에 범용적으로 쓰는 이벤트 핸들러입니다.
     */
    @JvmStatic
    @BindingAdapter(value = ["android:onCheckedAttrChanged"], requireAll = false)
    fun ChipGroup.setEventListener(attrChanged: InverseBindingListener?) {
        setOnCheckedStateChangeListener { group, checkedIds ->
            attrChanged?.onChange()
        }
    }

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
    fun ChipGroup.getDorm(): Dorm? {
        return Dorm.fromElementId(checkedChipId)
    }


    @JvmStatic
    @BindingAdapter("selectedJoinPeriod")
    fun ChipGroup.setJoinPeriod(joinPeriod: JoinPeriod?) = joinPeriod?.elementId?.let { check(it) }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedJoinPeriod", event = "android:onCheckedAttrChanged")
    fun ChipGroup.getJoinPeriod(): JoinPeriod? {
        return JoinPeriod.fromElementId(checkedChipId)
    }
}

object ChipBinding {
    /**
     * ChipGroup에 범용적으로 쓰는 이벤트 핸들러입니다.
     */
    @JvmStatic
    @BindingAdapter(value = ["android:onCheckedAttrChanged"], requireAll = false)
    fun ChipGroup.setEventListener(attrChanged: InverseBindingListener?) {
        setOnCheckedStateChangeListener { group, checkedIds ->
            attrChanged?.onChange()
        }
    }

    @JvmStatic
    @BindingAdapter("selectedValues")
    fun ChipGroup.setSelectedValues(selectedValues: List<Int>) {
        IDormLogger.i(this, "$selectedValues")
        selectedValues.forEach {
            check(it)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedValues", event = "android:onCheckedAttrChanged")
    fun ChipGroup.getSelectedValues(): List<Int> = checkedChipIds


}
