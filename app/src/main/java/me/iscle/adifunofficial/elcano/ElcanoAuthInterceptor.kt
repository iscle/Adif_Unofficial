package me.iscle.adifunofficial.elcano

import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

// Adif / Elcano constants
private const val ADIF_ACCESS_KEY = "and20210615"
private const val ADIF_SECRET_KEY = "Jthjtr946RTt"
private const val ELCANO_CLIENT = "AndroidElcanoApp"
private const val ELCANO_REQUEST = "elcano_request"
private const val ELCANO_CONTENT_TYPE = "application/json;charset=utf-8"

// Headers
private const val HEADER_X_ELCANO_HOST = "X-Elcano-Host"
private const val HEADER_CONTENT_TYPE = "Content-Type"
private const val HEADER_X_ELCANO_CLIENT = "X-Elcano-Client"
private const val HEADER_X_ELCANO_DATE = "X-Elcano-Date"
private const val HEADER_X_ELCANO_USERID = "X-Elcano-UserId"
private const val HEADER_AUTHORIZATION = "Authorization"

class ElcanoAuthInterceptor(
    private val androidId: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val host = request.url.host
        val path = request.url.encodedPath
        val query = request.url.query ?: ""
        val method = request.method
        val body = request.body.let { body ->
            val buffer = Buffer()
            body?.writeTo(buffer)
            buffer.readUtf8()
        }

        val date = Date()
        val elcanoDate = getElcanoDate(date)
        val elcanoDateSimple = getElcanoDateSimple(date)
        val authorization = getAuthorization(host, path, query, method, body, elcanoDate, elcanoDateSimple)

        val newRequest = request.newBuilder()
            .addHeader(HEADER_X_ELCANO_HOST, host)
            .addHeader(HEADER_CONTENT_TYPE, ELCANO_CONTENT_TYPE)
            .addHeader(HEADER_X_ELCANO_CLIENT, ELCANO_CLIENT)
            .addHeader(HEADER_X_ELCANO_DATE, elcanoDate)
            .addHeader(HEADER_X_ELCANO_USERID, androidId)
            .addHeader(HEADER_AUTHORIZATION, authorization)
            .build()

        return chain.proceed(newRequest)
    }

    private fun getElcanoDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    private fun getElcanoDateSimple(date: Date): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    private fun getSignature(
        canonicalRequest: String,
        elcanoDate: String,
        elcanoDateSimple: String,
    ): String {
        val signedDate = hmacSha256(ADIF_SECRET_KEY.toByteArray(), elcanoDateSimple)
        val signedClient = hmacSha256(signedDate, ELCANO_CLIENT)
        val signedRequest = hmacSha256(signedClient, ELCANO_REQUEST)
        val stringToSign = "HMAC-SHA256\n" +
                "$elcanoDate\n" +
                "$elcanoDateSimple/" +
                "$ELCANO_CLIENT/" +
                "$androidId/" +
                "$ELCANO_REQUEST\n" +
                sha256Hex(canonicalRequest)
        val signature = hmacSha256(signedRequest, stringToSign)
        return bytesToHex(signature)
    }

    private fun getCanonicalRequest(
        method: String,
        path: String,
        query: String,
        host: String,
        elcanoDate: String,
        body: String,
    ): String {
        return "$method\n" +
                "$path\n" +
                "$query\n" +
                "${HEADER_CONTENT_TYPE.lowercase()}:$ELCANO_CONTENT_TYPE\n" +
                "${HEADER_X_ELCANO_HOST.lowercase()}:$host\n" +
                "${HEADER_X_ELCANO_CLIENT.lowercase()}:$ELCANO_CLIENT\n" +
                "${HEADER_X_ELCANO_DATE.lowercase()}:$elcanoDate\n" +
                "${HEADER_X_ELCANO_USERID.lowercase()}:$androidId\n" +
                "${HEADER_CONTENT_TYPE.lowercase()};" +
                "${HEADER_X_ELCANO_HOST.lowercase()};" +
                "${HEADER_X_ELCANO_CLIENT.lowercase()};" +
                "${HEADER_X_ELCANO_DATE.lowercase()};" +
                "${HEADER_X_ELCANO_USERID.lowercase()}\n" +
                sha256Hex(body.replace("\r", "").replace("\n", "").replace(" ", ""))
    }

    private fun getAuthorization(host: String, path: String, query: String, method: String, body: String, elcanoDate: String, elcanoDateSimple: String): String {
        val canonicalRequest = getCanonicalRequest(method, path, query, host, elcanoDate, body)
        val signature = getSignature(canonicalRequest, elcanoDate, elcanoDateSimple)
        return "HMAC-SHA256 Credential=" +
                "$ADIF_ACCESS_KEY/" +
                "$elcanoDateSimple/" +
                "$ELCANO_CLIENT/" +
                "$androidId/" +
                "elcano_request," +
                "SignedHeaders=" +
                "content-type;" +
                "x-elcano-host;" +
                "x-elcano-client;" +
                "x-elcano-date;" +
                "x-elcano-userid," +
                "Signature=" +
                signature
    }

    private fun hmacSha256(key: ByteArray, data: String): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key, "HmacSHA256"))
        return mac.doFinal(data.toByteArray())
    }

    private fun sha256Hex(data: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(data.toByteArray())
        return bytesToHex(digest)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }
}