package com.js.movietrends

import com.js.movietrends.ui.utils.FormatUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatUtilTest {
    @Test
    fun format_isOneDecimal() {
        assertEquals("3.1", FormatUtil.formatToOneDecimal(3.14f))
        assertEquals("5", FormatUtil.formatToOneDecimal(5.0f))
    }
}