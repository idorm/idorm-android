package org.appcenter.inudorm.model


data class OnboardQuestion(
    var questionText: String,
    var isRequired: String?,
    var maxLen : Int,
    //var validator: (String) -> Boolean

)