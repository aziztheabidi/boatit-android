package com.boatit.boatsharing.ui.voyager.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class VoyageCategoryDropdownResponse(
    val Status: Int,
    val Message: String,
    val obj: List<VoyageCategory>?
)

@Serializable
data class VoyageCategory(
    val ParentId: Int,
    val Id: Int,
    val Name: String
)
