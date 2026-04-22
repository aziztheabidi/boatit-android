package com.boatit.boatsharing.features.voyager.dashboard.domain.model

import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorPayments
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagerPayment

data class SponsorPaymentsDomainModel(
    val status: Int,
    val message: String,
    val voyages: List<SponsorVoyageDomainModel>,
)

data class SponsorVoyageDomainModel(
    val id: String,
    val name: String,
    val voyagerName: String,
    val voyagerPhoneNumber: String,
    val pickupDock: String,
    val pickupDockLatitude: Double,
    val pickupDockLongitude: Double,
    val dropOffDock: String,
    val dropOffDockLatitude: Double,
    val dropOffDockLongitude: Double,
    val amountToPay: Double,
    val noOfVoyagers: Int,
    val waterStay: String,
    val duration: String,
    val bookingDateTime: String,
    val voyageStatus: String,
)

fun SponsorPayments.toDomainModel(): SponsorPaymentsDomainModel {
    return SponsorPaymentsDomainModel(
        status = Status,
        message = Message,
        voyages = obj.map { it.toDomainModel() },
    )
}

fun SponsorVoyagerPayment.toDomainModel(): SponsorVoyageDomainModel {
    return SponsorVoyageDomainModel(
        id = Id,
        name = Name,
        voyagerName = VoyagerName,
        voyagerPhoneNumber = VoyagerPhoneNumber,
        pickupDock = PickupDock,
        pickupDockLatitude = PickupDockLatitude,
        pickupDockLongitude = PickupDockLongitude,
        dropOffDock = DropOffDock,
        dropOffDockLatitude = DropOffDockLatitude,
        dropOffDockLongitude = DropOffDockLongitude,
        amountToPay = AmountToPay,
        noOfVoyagers = NoOfVoyagers,
        waterStay = WaterStay,
        duration = Duration,
        bookingDateTime = BookingDateTime,
        voyageStatus = VoyageStatus,
    )
}
