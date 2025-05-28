import kotlinx.serialization.Serializable

@Serializable
data class CaptainActiveVoyagesResponse(
    val Status: Int,
    val Message: String,
    val obj: CaptainActiveVoyagesObj
)

@Serializable
data class CaptainActiveVoyagesObj(
    val Pending: List<VoyageData>,
    val Accepted: List<VoyageData>,
    val Started: List<VoyageData>
)

@Serializable
data class VoyageData(
    val Id: String,
    val Name: String,
    val VoyagerUserId: String,
    val VoyagerName: String,
    val VoyagerPhoneNumber: String,
    val PickupDock: String,
    val PickupDockLatitude: Double,
    val PickupDockLongitude: Double,
    val DropOffDock: String,
    val DropOffDockLatitude: Double,
    val DropOffDockLongitude: Double,
    val NoOfVoyager: Int,
    val BookingDateTime: String,
    val AmountToPay: Double,
    val WaterStay: String,
    val Duration: String
)
