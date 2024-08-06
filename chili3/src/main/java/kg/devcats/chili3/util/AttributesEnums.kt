package kg.devcats.chili3.util

enum class StoriesStatus(val value: Int) {
    VIEWED(0), UNVIEWED(1)
}

enum class PromoStatus(val value: Int) {
    NO_STATUS(0), NEW(1), ACTIVE(2), WAIT(3), EXPIRED(4)
}

enum class ViewSize(val value: Int) {
    SMALL(-1), MEDIUM(-2), LARGE(-3)
}

enum class PackageType {
    CALL, INTERNET
}

enum class ButtonType(val value: Int) {
    PRIMARY(0), ADDITIONAL(1)
}