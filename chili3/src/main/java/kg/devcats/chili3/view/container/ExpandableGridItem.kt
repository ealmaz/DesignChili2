package kg.devcats.chili3.view.container

data class ExpandableGridItem(
    var id: Long? = null,
    var title: String?,
    val icon: String?,
    var deeplink: String?,
    val isShimmering: Boolean = false
)