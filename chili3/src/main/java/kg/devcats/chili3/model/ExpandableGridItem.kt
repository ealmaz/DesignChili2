package kg.devcats.chili3.model

data class ExpandableGridItem(
    var id: Long? = null,
    val title: String?,
    val icon: String?,
    val deeplink: String?,
    val isShimmering: Boolean = false
)