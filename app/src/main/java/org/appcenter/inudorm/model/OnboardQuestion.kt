package org.appcenter.inudorm.model

import android.view.View


data class OnboardQuestion(
    var questionText: String,
    var isRequired: String?,
    var visibility : Int? = View.GONE,
    var maxLen : String? = "100",
    var currentLen : String? = "0",




    //var validator: (String) -> Boolean

)