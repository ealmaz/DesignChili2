package com.design2.chili2.util

import android.content.res.Resources
import com.design2.chili2.R
import com.design2.chili2.util.PackageLeftOverUtils.NetworkUnits.GIGABYTE
import com.design2.chili2.util.PackageLeftOverUtils.NetworkUnits.TERRABYTE
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs

class PackageLeftOverUtils {

    companion object {
        fun readableFileSize(size: Long, resources: Resources, prefix: String? = null, isWithoutUnitName: Boolean = false, isM2M: Boolean = false): String {
            val units = resources.getStringArray(R.array.network_units)
            val formattedPrefix = if (prefix != null) "$prefix " else ""
            val unitMultiplier = 1024.0
            val digitGroups = (Math.log(size.toDouble()) / Math.log(unitMultiplier)).toInt()
            val unitsFromSize = size / Math.pow(unitMultiplier, digitGroups.toDouble())
            if (size <= 0)  {
                when {
                    isWithoutUnitName -> return "0 "
                    isM2M -> return "$formattedPrefix${unitsFromSize.toInt()} ${units[units.count() - 3]}" //return 0 МБ
                    !isWithoutUnitName -> return "0 ${units[units.count() - 2]}" //return 0 ГБ
                }
            }
            val unitName = units[digitGroups]
            if (isWithoutUnitName) {
                return "$formattedPrefix${unitsFromSize.toThreeDigitsFormat}"
            }
            return when (digitGroups) {
                GIGABYTE, TERRABYTE -> "$formattedPrefix${unitsFromSize.toThreeDigitsFormat} $unitName"
                else -> "$formattedPrefix${unitsFromSize.toInt()} $unitName"
            }
        }
    }

    object NetworkUnits {
        val BYTE = 0
        val KILLOBYTE = 1
        val MEGABYTE = 2
        val GIGABYTE = 3
        val TERRABYTE = 4
    }
}

val Double.toThreeDigitsFormat: String
    get() {
        return when {
            (abs(this) < 10.0) -> this.round("0.00")
            (abs(this) in 10.0..100.0) -> this.round("#.0")
            else -> this.round("#")
        }.replace(".", ",")
    }

private fun Double.round(format: String = "#.##", roundingMode: RoundingMode? = null): String {
    val df = DecimalFormat(format)
    roundingMode?.let { df.roundingMode = roundingMode }
    return df.format(this).replace(".", ",")
}