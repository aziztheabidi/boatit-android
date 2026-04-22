package com.boatit.boatsharing.features.captain.domain.model

import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyage
import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyageResponse

data class CaptainCompletedVoyagesDomainModel(
    val status: Int,
    val message: String,
    val voyages: List<CaptainCompletedVoyageDomainModel>,
)

data class CaptainCompletedVoyageDomainModel(
    val id: String,
    val name: String,
    val voyagerUserId: String,
    val voyagerName: String,
    val voyagerPhoneNumber: String,
    val rating: Double?,
    val pickupDock: String,
    val pickupDockLatitude: Double,
    val pickupDockLongitude: Double,
    val dropOffDock: String,
    val dropOffDockLatitude: Double,
    val dropOffDockLongitude: Double,
    val noOfVoyager: Int,
    val amountToPay: Double,
    val waterStay: String,
    val duration: String,
    val bookingDateTime: String,
)

fun CaptainCompletedVoyageResponse.toDomainModel(): CaptainCompletedVoyagesDomainModel {
    return CaptainCompletedVoyagesDomainModel(
        status = Status,
        message = Message,
        voyages = obj.map { it.toDomainModel() },
    )
}

fun CaptainCompletedVoyage.toDomainModel(): CaptainCompletedVoyageDomainModel {
    return CaptainCompletedVoyageDomainModel(
        id = Id,
        name = Name,
        voyagerUserId = VoyagerUserId,
        voyagerName = VoyagerName,
        voyagerPhoneNumber = VoyagerPhoneNumber,
        rating = Rating,
        pickupDock = PickupDock,
        pickupDockLatitude = PickupDockLatitude,
        pickupDockLongitude = PickupDockLongitude,
        dropOffDock = DropOffDock,
        dropOffDockLatitude = DropOffDockLatitude,
        dropOffDockLongitude = DropOffDockLongitude,
        noOfVoyager = NoOfVoyager,
        amountToPay = AmountToPay,
        waterStay = WaterStay,
        duration = Duration,
        bookingDateTime = BookingDateTime,
    )
}
