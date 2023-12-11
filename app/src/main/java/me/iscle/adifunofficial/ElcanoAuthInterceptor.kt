package me.iscle.adifunofficial

import okhttp3.Interceptor
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class ElcanoAuthInterceptor(
    private val androidId: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val date = Date()

        val newRequest = request.newBuilder()
            .addHeader("X-Elcano-Host", request.url.host)
            .addHeader("Content-Type", "application/json;charset=utf-8")
            .addHeader("X-Elcano-Client", "AndroidElcanoApp")
            .addHeader("X-Elcano-Date", getXElcanoDate(date))
            .addHeader("X-Elcano-UserId", androidId)
            .addHeader("Authorization", getAuthorization(date))
            .build()

        return chain.proceed(newRequest)
    }

    private fun getXElcanoDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    private fun getElcanoDateSimple(date: Date): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    private fun hmacSha256(key: ByteArray, data: String): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key, "HmacSHA256"))
        return mac.doFinal(data.toByteArray())
    }

    private fun getAuthorization(date: Date): String {
        val xElcanoDateSimple = getElcanoDateSimple(date)
        val signature = ""
        
        return "HMAC-SHA256 Credential=and20210615/$xElcanoDateSimple/AndroidElcanoApp/$androidId/elcano_request,SignedHeaders=content-type;x-elcano-host;x-elcano-client;x-elcano-date;x-elcano-userid,Signature=$signature"
    }
}