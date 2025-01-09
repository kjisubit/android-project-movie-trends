package com.js.movietrends.domain.core

import java.util.Locale

object AppInfoManager {
    var localeCode: String = "en-US"

    init {
        localeCode = Locale.getDefault().toString().replace("_", "-")
    }
}