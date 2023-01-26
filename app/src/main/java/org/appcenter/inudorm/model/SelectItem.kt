package org.appcenter.inudorm.model

data class SelectItem(
    val title: String,
    val value: String,
    val iconResourceId: Int? = null,
    val desc: String? = null
)