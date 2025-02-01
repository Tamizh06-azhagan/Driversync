data class DriverBookingListModel(
    val id: Int,
    val userid: Int,
    val username: String,  // Add username field
    val dateofbooking: String,
    val status: String
)
data class DriverBookingListResponse(
    val status: Boolean,
    val message: String,
    val data: List<DriverBookingListModel>
)
