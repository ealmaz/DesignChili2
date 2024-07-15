package kg.devcats.chili3.extensions

import android.graphics.Typeface
import android.widget.TextView

fun TextView.setNormalTextWeight() {
    typeface = Typeface.create(typeface, Typeface.NORMAL)
}

fun TextView.setBoldTextWeight() {
    setTypeface(typeface, Typeface.BOLD)
}