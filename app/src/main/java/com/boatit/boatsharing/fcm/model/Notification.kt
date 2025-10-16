package com.boatit.boatsharing.ui.voyager.dashbaord.model


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
    val Rating: String? = null,
    val PastVoyages: String?,
    val Title: String?,
    val Body: String?,
    val CaptainUserId: String?,
    val BookingDateTime: String?= null,
    val WaterStay: String?= null,
    val Duration: String? = null,

) : Parcelable {
    override fun describeContents(): Int { return 0 }
    override fun writeToParcel(p0: Parcel, p1: Int) {}
}