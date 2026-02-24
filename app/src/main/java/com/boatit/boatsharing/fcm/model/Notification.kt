package com.boatit.boatsharing.ui.voyager.dashboard.model


import android.os.Parcel
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
    val PastVoyages: String?,
    val Title: String?,
    val Body: String?,
    val CaptainUserId: String?
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
    }
}