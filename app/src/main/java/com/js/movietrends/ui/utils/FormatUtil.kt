package com.js.movietrends.ui.utils

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