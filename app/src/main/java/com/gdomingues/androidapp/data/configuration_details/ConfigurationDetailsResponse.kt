package com.gdomingues.androidapp.data.configuration_details

import com.squareup.moshi.Json

data class ConfigurationDetailsResponse(
    val images: ImagesConfigurationResponse,
    @Json(name = "change_keys") val changeKeys: List<String>
)
