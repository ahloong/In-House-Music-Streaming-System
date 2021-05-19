package com.loong.ihms.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.IOException

class LogJsonInterceptor : Interceptor {           //for logcat
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val rawResponseString = response.body?.string() ?: ""

        if (rawResponseString.isNotEmpty()) {
            try {
                var formattedString = rawResponseString

                try {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val jsonElement = JsonParser.parseString(rawResponseString)

                    formattedString = gson.toJson(jsonElement)
                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

                val fullLog = "ihms_api_call: ${request.url}\n${formattedString}"
                Timber.i(fullLog)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        // Re-create the response before returning it because body can be read only once
        val defaultMediaType = "application/json".toMediaTypeOrNull()
        val responseMediaType = response.body?.contentType() ?: defaultMediaType

        return response
            .newBuilder()
            .body(rawResponseString.toResponseBody(responseMediaType))
            .build()
    }
}