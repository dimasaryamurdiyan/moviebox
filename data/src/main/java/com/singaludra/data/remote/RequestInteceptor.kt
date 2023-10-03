package com.singaludra.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = "BuildConfig.API_KEY"
        val httpUrlBuilder = original.url.newBuilder()
        val httpUrl = httpUrlBuilder.addQueryParameter("api_key", token).build()

        val request = original.newBuilder()
            .url(httpUrl)
            .build()

        return chain.proceed(request)
    }
}