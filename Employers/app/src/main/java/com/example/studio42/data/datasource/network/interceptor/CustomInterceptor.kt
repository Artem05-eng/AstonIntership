package com.example.studio42.data.datasource.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString().replace("%26", "&")
        val newRequest = Request.Builder()
            .url(url)
            .build()
        return chain.proceed(newRequest)
    }
}