package com.boatit.boatsharing.features.business.model

import kotlinx.serialization.Serializable

@Serializable
data class DocksDropdownResponse(
    val Status: Int,
    val Message: String,
    val obj: DockDropdownObj?,
)

@Serializable
data class DockDropdownObj(
    val Shore: List<DockDropdownItem>?,
    val Zone: List<DockDropdownItem>?,
    val Island: List<DockDropdownItem>?,
)

@Serializable
data class DockDropdownItem(
    val ParentId: Int,
    val Id: Int,
    val Name: String,
)
