package kg.devcats.chili3.model

data class MyConnectionProfile(
    var balance: CharSequence? = null,
    var tariffName: String? = null,
    var withoutPackageSubTitle: String? = null,
    var isWithPackages: Boolean = false,
    var packages: List<PackageLeftOver>? = null
)

data class PackageLeftOver(
    var packageType: PackageType? = null,
    var remain: String? = null,
    var limit: String? = null,
    var isUnlimited: Boolean = false,
    var leftOverPercent: Int = 0
)

enum class PackageType {
    CALL, INTERNET
}