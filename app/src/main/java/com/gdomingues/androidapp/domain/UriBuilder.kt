package com.gdomingues.androidapp.domain

import android.net.Uri

interface UriBuilder {
    fun buildUri(baseUrl: String, backdropSizes: List<String>, backdropPath: String): Uri
}
