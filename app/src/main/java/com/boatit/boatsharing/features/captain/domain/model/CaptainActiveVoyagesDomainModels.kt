package com.boatit.boatsharing.features.captain.domain.model

import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesResponse

data class CaptainActiveVoyagesDomainModel(
    val status: Int,
    val message: String,
    val pending: List<CaptainVoyageDomainModel>,
    val accepted: List<CaptainVoyageDomainModel>,
    val started: List<CaptainVoyageDomainModel>,
)

data class CaptainVoyageDomainModel(
    val id: String,
    val name: String,
    val voyagerUserId: String,
    val voyagerName: String,
    val voyagerPhoneNumber: String,
    val pickupDock: String,
    val pickupDockLatitude: Double,
    val pickupDockLongitude: Double,
    val dropOffDock: String,
    val dropOffDockLatitude: Double,
    val dropOffDockLongitude: Double,
    val noOfVoyager: Int,
    val bookingDateTime: String,
    val amountToPay: Double,
    val waterStay: String,
    val duration: String,
)

fun CaptainActiveVoyagesResponse.toDomainModel(): CaptainActiveVoyagesDomainModel {
    return CaptainActiveVoyagesDomainModel(
        status = Status,
        message = Message,
        pending = obj.Pending.orEmpty().map { it.toDomainModel() },
        accepted = obj.Accepted.orEmpty().map { it.toDomainModel() },
        started = obj.Started.orEmpty().map { it.toDomainModel() },
    )
}

private fun com.boatit.boatsharing.features.captain.dashboard.model.VoyageData.toDomainModel(): CaptainVoyageDomainModel {
    return CaptainVoyageDomainModel(
        id = Id,
        name = Name,
        voyagerUserId = VoyagerUserId,
        voyagerName = VoyagerName,
        voyagerPhoneNumber = VoyagerPhoneNumber,
        pickupDock = PickupDock,
        pickupDockLatitude = PickupDockLatitude,
        pickupDockLongitude = PickupDockLongitude,
        dropOffDock = DropOffDock,
        dropOffDockLatitude = DropOffDockLatitude,
        dropOffDockLongitude = DropOffDockLongitude,
        noOfVoyager = NoOfVoyager,
        bookingDateTime = BookingDateTime,
        amountToPay = AmountToPay,
        waterStay = WaterStay,
        duration = Duration,
    )
}
