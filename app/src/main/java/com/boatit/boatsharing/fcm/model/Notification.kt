package com.boatit.boatsharing.features.voyager.dashboard.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoyageNotification(
    val Id: String?,
    val Name: String?,
    val PhoneNumber: String?,
    val PickupDock: String?,
    val DropOffDock: String?,
    val NoOfVoyager: String?,
    val TotalAmount: String?,
    val Rating: String? = null,
    val PastVoyages: String?,
    val Title: String?,
    val Body: String?,
    val CaptainUserId: String?,
    val BookingDateTime: String? = null,
    val WaterStay: String? = null,
    val Duration: String? = null,
) : Parcelable
