package com.gdomingues.androidapp.data

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

internal class AuthInterceptor @Inject constructor() : Interceptor {

    private val ACCESS_TOKEN = ""

    override fun intercept(chain: Chain): Response {
        val builder = chain.request()
            .newBuilder()
            .header("Authorization", "Bearer $ACCESS_TOKEN")

        return chain.proceed(builder.build())
    }
}
