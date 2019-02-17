package com.cjj.keepaccounts.http

import android.text.TextUtils
import android.util.Log
import com.cjj.keepaccounts.utils.LogUtils
import com.lzy.okgo.utils.IOUtils
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by CJJ on 2018/6/25 22:35.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class HttpLogInterceptor(private val level: Int, private val tag: String) : Interceptor {
    private val utf8 = Charset.forName("UTF-8")


    private val topBorder = "╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private val line = "────────────────────────────────────────────────────────────────────────────────────"
    private val bottomBorder = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private val leftChar = "║"


    override fun intercept(chain: Interceptor.Chain): Response {
        val sb = StringBuilder()
        var request = chain.request()
        val codeAddress = request.headers().get("codeLine")
        if (!TextUtils.isEmpty(codeAddress)) {
            sb.append("\t\t")
            sb.append(codeAddress)
            sb.append("\n")
            sb.append(line)
            sb.append("\n")
            request = request.newBuilder().removeHeader("codeLine").build()
        }

        logForRequest(request, chain.connection(), sb)

        //执行请求，计算请求时间
        val startNs = System.nanoTime()
        var response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            LogUtils.exception(e)
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        response = logForResponse(response, tookMs, sb)
        log(level, tag, sb.toString())
        sb.setLength(0)
        return response
    }

    private fun logForRequest(request: Request, connection: Connection?, sb: StringBuilder) {
        val requestBody = request.body()
        val hasRequestBody = requestBody != null
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        try {
            sb.append("\t\t")
                    .append(request.method())
                    .append("\t")
                    .append(protocol)
                    .append("\t")
                    .append(URLDecoder.decode(request.url().toString(), "UTF-8"))
                    .append("\n")

            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    sb.append("\t        at ")
                    sb.append("Content-Type")
                    sb.append(": ")
                    sb.append(requestBody.contentType())
                    sb.append(" \n")
                }
                if (requestBody.contentLength() != -1L) {
                    sb.append("\t        at ")
                    sb.append("Content-Length")
                    sb.append(": ")
                    sb.append(requestBody.contentLength())
                    sb.append(" \n")
                }
            }
            val headers = request.headers()

            val count = headers.size()
            for (i in 0 until count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    sb.append("\t        at ")
                    sb.append(name)
                    sb.append(": ")
                    sb.append(headers.value(i))
                    sb.append(" \n")
                }
            }
            sb.append(line)

            if (hasRequestBody) {
                if (isPlaintext(requestBody!!.contentType())) {
                    sb.append("\n")
                    sb.append("\t\tRequest:")
                    sb.append("\n")
                    sb.append(bodyToString(request))
                    sb.append("\n")
                } else {
                    sb.append("\tbody: maybe [binary body], omitted!\n")
                }
            }

        } catch (e: Exception) {
            LogUtils.exception(e)
        } finally {
            sb.append(line)
            sb.append("\n")
        }
    }

    private fun logForResponse(r: Response, tookMs: Long, sb: StringBuilder): Response {
        var response = r
        val builder = response.newBuilder()
        val clone = builder.build()
        var responseBody = clone.body()

        try {
            sb.append("\t\t")
                    .append(clone.message())
                    .append("\t")
                    .append(clone.code())
                    .append("\t")
                    .append("(")
                    .append(tookMs)
                    .append("ms)")
                    .append(" \n")
            val headers = clone.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                sb.append("\t        at ")
                sb.append(headers.name(i))
                sb.append(": ")
                sb.append(headers.value(i))
                sb.append(" \n")
                i++
            }
            if (HttpHeaders.hasBody(clone)) {
                if (responseBody == null) return response

                if (isPlaintext(responseBody.contentType())) {
                    val bytes = IOUtils.toByteArray(responseBody.byteStream())
                    val contentType = responseBody.contentType()
                    var body = String(bytes, getCharset(contentType))
                    sb.append("\n")
                    sb.append("\t\t")
                    sb.append("Response: ")
                    sb.append("\n")
                    body = formatJson(body)
                    sb.append(body)
                    responseBody = ResponseBody.create(responseBody.contentType(), bytes)
                    response = response.newBuilder().body(responseBody).build()
                } else {
                    sb.append("\t\t\t\tbody: maybe [binary body], omitted!")
                }
            }

        } catch (e: Exception) {
            LogUtils.exception(e)
        }

        return response

    }


    private fun formatJson(b: String): String {
        var body = b
        val stringBuilder = StringBuilder()
        val split: Array<String>
        if (body.startsWith("{")) {
            val jsonObject = JSONObject(body)
            body = jsonObject.toString(2)
        } else if (body.startsWith("[")) {
            val jsonArray = JSONArray(body)
            body = jsonArray.toString(2)
        }

        split = body.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val `is` = split.size <= 20
        for (s in split) {
            if (`is`) {
                stringBuilder.append("\t\t\t")
            } else {
                stringBuilder.append("\t        at \t\t")
            }
            stringBuilder.append(s)
            stringBuilder.append(" \n")
        }
        return stringBuilder.toString()
    }

    private fun bodyToString(request: Request): String {
        try {
            val copy = request.newBuilder().build()
            val body = copy.body() ?: return ""
            val buffer = Buffer()
            body.writeTo(buffer)
            val mediaType = body.contentType()
            val charset = getCharset(mediaType)
            var str = buffer.readString(charset)
            str = URLDecoder.decode(str, "UTF-8")
            if (mediaType != null) {
                when (mediaType.subtype()) {
                    "x-www-form-urlencoded"//表单
                    -> {
                        val stringBuilder = StringBuilder()
                        val split = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (s in split) {
                            stringBuilder.append("\t\t\t")
                            stringBuilder.append(s)
                            stringBuilder.append("\n")
                        }
                        str = stringBuilder.toString()
                    }
                    "json"//json
                    -> str = formatJson(str)
                    else -> {
                    }
                }
            } else {
                str = "\t\t\t" + str
            }


            return str
        } catch (e: Exception) {
            LogUtils.exception(e)
            return "error"
        }

    }

    private fun isPlaintext(mediaType: MediaType?): Boolean {
        if (mediaType == null) return false
        if (mediaType.type() != null && mediaType.type() == "text") {
            return true
        }
        var subtype = mediaType.subtype()
        if (subtype != null) {
            subtype = subtype.toLowerCase()
            return subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html")
        }
        return false
    }

    private fun getCharset(contentType: MediaType?): Charset {
        return if (contentType != null) contentType.charset(utf8)!! else utf8
    }

    private var tagNum = 0
    private val random = Random()

    @Synchronized
    private fun log(priority: Int, tag: String, msg: String) {

        val split = msg.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        Log.println(priority, getTag(tag), topBorder)
        for (line in split) {
            Log.println(priority, getTag(tag), leftChar + line)
        }
        Log.println(priority, getTag(tag), bottomBorder)
    }

    private fun getTag(tag: String): String {
        var rNum = 0
        while (tagNum == rNum) {
            rNum = random.nextInt(9)
        }
        tagNum = rNum
        return tag + tagNum
    }
}