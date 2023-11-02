package com.design2.chili2.util

val cyrillicRegex = "^([а-яА-ЯҢңӨөҮүёЁ]|-| )+".toRegex()
val noLatinCharsRegex = "^[^a-zA-Z]+$".toRegex()