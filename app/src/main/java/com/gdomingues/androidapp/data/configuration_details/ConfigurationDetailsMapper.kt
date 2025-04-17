package com.gdomingues.androidapp.data.configuration_details

fun ConfigurationDetailsResponse.toDomain(): ConfigurationDetails {
    return ConfigurationDetails(
        images = images.toDomain(),
        changeKeys = changeKeys
    )
}

fun ImagesConfigurationResponse.toDomain(): ImagesConfiguration {
    return ImagesConfiguration(
        baseUrl = baseUrl,
        secureBaseUrl = secureBaseUrl,
        backdropSizes = backdropSizes,
        logoSizes = logoSizes,
        posterSizes = posterSizes,
        profileSizes = profileSizes,
        stillSizes = stillSizes
    )
}
