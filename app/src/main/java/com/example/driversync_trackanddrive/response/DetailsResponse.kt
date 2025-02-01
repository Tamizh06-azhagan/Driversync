data class DetailsResponse(
    val status: Boolean,
    val message: String,
    val userid: Int,
    val bookings: List<DetailsData>
)

data class DetailsData(
    val booking_id: Int,
    val date: String,
    val drivername: String,
    val status: String
)
