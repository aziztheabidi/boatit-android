package com.boatit.boatsharing.features.voyager.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class CalculateFair(
    val Status: Int,
    val Message: String,
    val obj: DockRate,
)

@Serializable
data class DockRate(
    val PerHourRate: Double,
    val TotalFair: Double,
)
