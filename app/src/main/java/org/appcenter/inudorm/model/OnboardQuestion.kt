package org.appcenter.inudorm.model

import android.view.View


data class OnboardQuestion(
    var questionText: String,
    var isRequired: String?,
    var visibility: Int? = View.GONE,
    var maxLen: Int? = 16,
    var currentLen: Int? = 0,
    var answer: String = "",
    var placeholder: String = "입력",

    //var validator: (String) -> Boolean

)