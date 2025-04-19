package com.gdomingues.androidapp.domain

import android.net.Uri
import androidx.core.net.toUri
import javax.inject.Inject

class DefaultUriBuilder @Inject constructor() : UriBuilder {

    /**
     * Builds a URL according to the specification of the The Movie Database API for building images
     *
     * https://developer.themoviedb.org/docs/image-basics
     */
    override fun buildUri(baseUrl: String, backdropSizes: List<String>, backdropPath: String): Uri {
        return baseUrl.toUri().buildUpon().apply {
            appendEncodedPath(backdropSizes.first()) // Append the first backdrop size (e.g., "w780")
            backdropPath.toUri().pathSegments.forEach { appendPath(it) } // Append each segment of the backdrop path
        }.build()
    }
}
