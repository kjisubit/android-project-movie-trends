package com.js.movietrends.ui.util

import java.text.DecimalFormat

object FormatUtil {
    fun formatToOneDecimal(value: Float): String {
        val decimalFormat = DecimalFormat("#.#")
        return decimalFormat.format(value)
    }

    fun formatToOneDecimal(value: Double): String {
        val decimalFormat = DecimalFormat("#.#")
        return decimalFormat.format(value)
    }
}