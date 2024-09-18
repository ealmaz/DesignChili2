package kg.devcats.chili3.model

import android.content.res.Resources
import android.text.Spanned
import androidx.core.text.parseAsHtml
import com.design2.chili2.util.PackageLeftOverUtils
import com.design2.chili2.R as Chili2R

data class MyConnectionProfile(
    var balance: CharSequence? = null,
    var tariffName: String? = null,
    var isWithPackages: Boolean = false,
    var hasData: Boolean = false,
    var packages: List<PackageLeftOver>? = null
) {
    fun getFormattedTariffName(prefix: String?): String {
        return StringBuilder().apply {
            append(prefix)
            append(": ")
            append(tariffName)
        }.toString()
    }
}

data class PackageLeftOver(
    var packageType: PackageType? = null,
    var remain: Long = 0,
    var limit: Long = 0,
    var isUnlimited: Boolean = false,
    var isSuspended: Boolean = false
) {

    fun getLeftOverPercentage(): Int {
        return when {
            limit == 0L -> 0
            remain > limit -> 100
            else -> (100 * remain / limit).toInt()
        }
    }

    fun isUnlimitedAndNotSuspended() = isUnlimited && !isSuspended

    fun isPackageEmpty(): Boolean = remain <= 0L && !isSuspended

    fun getRemainingMinutesWithFormat(minutes: String): Spanned {
        return ("" + (remain / 60) + " " + minutes).parseAsHtml()
    }

    fun getLimitMinutesWithFormat(minutes: String): Spanned {
        return ("" + (limit / 60) + " " + minutes).parseAsHtml()
    }

    fun getFormattedRemain(resources: Resources): CharSequence {
        return when (packageType) {
            PackageType.INTERNET -> {
                if (isSuspended || isPackageEmpty()) resources.getString(Chili2R.string.my_connection_card_internet_package_title)
                else PackageLeftOverUtils.readableFileSize(remain, resources).trim()
            }

            PackageType.CALL -> {
                if (isSuspended || isPackageEmpty()) resources.getString(Chili2R.string.my_connection_card_calling_package_title)
                else getRemainingMinutesWithFormat(resources.getString(com.design2.chili2.R.string.chili_minutes))
            }

            else -> ""
        }
    }

    fun getFormattedLimit(resources: Resources): CharSequence {
        return when (packageType) {
            PackageType.INTERNET -> resources.getString(
                com.design2.chili2.R.string.chili_leftover_from_limit,
                PackageLeftOverUtils.readableFileSize(limit, resources)
            )

            PackageType.CALL -> resources.getString(
                com.design2.chili2.R.string.chili_leftover_from_limit,
                getLimitMinutesWithFormat(resources.getString(com.design2.chili2.R.string.chili_minutes))
            )

            else -> ""
        }
    }
}

enum class PackageType {
    CALL, INTERNET
}