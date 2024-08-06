package kg.devcats.chili3.model

data class ExpandableGridItem(
    var id: Long? = null,
    val title: String? = null,
    val icon: String? = null,
    val deeplink: String? = null,
    val analyticsTag: String? = null,
    val isShimmering: Boolean = false
)