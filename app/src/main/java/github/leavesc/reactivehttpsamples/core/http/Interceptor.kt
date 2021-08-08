package github.leavesc.reactivehttpsamples.core.http

import com.google.gson.Gson
import github.leavesc.reactivehttpsamples.utils.DESUtils
import github.leavesc.reactivehttpsamples.utils.HmacUtils
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import okio.BufferedSink
import okio.GzipSink
import okio.buffer
import org.json.JSONObject
import java.io.IOException
import java.lang.StringBuilder
import java.nio.charset.Charset


/**
 * @Author: leavesC
 * @Date: 2020/2/25 16:10
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
class FilterInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpBuilder = originalRequest.url.newBuilder()
        httpBuilder.addEncodedQueryParameter(HttpConfig.KEY, HttpConfig.KEY_MAP)
        val requestBuilder = originalRequest.newBuilder()
            .url(httpBuilder.build())
        return chain.proceed(requestBuilder.build())
    }

}


/**
 * 请求消息摘要签名拦截器
 */
class ReqSignatureInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originReq = chain.request()
        val reqStrBuilder = StringBuilder(originReq.toString())
        if (originReq.body == null) {
            reqStrBuilder.append("")
        }
        val signature = HmacUtils.hmacSha256AndBase64(reqStrBuilder.toString(), "signaturekey")

        val newReqBuilder = originReq.newBuilder()
        newReqBuilder.header("X-Signature", signature)


        return chain.proceed(newReqBuilder.build())
    }
}

/**
 * 请求头拦截器
 */
class ReqHeaderInterceptor : Interceptor {
    /**
     * 头部哈希表，临时存放头部key-value
     */
    private val headsMap: LinkedHashMap<String, String> = LinkedHashMap()

    /**
     * 添加头部
     * @param key 字段key
     * @param value 字段值
     */
    fun addHead(key: String, value: String) {
        headsMap[key] = value
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originReq = chain.request()
        val newReqBuilder = originReq.newBuilder()

        headsMap.forEach {
            newReqBuilder.header(it.key, it.value)
        }

        return chain.proceed(newReqBuilder.build())
    }
}

/**
 * 请求查询参数拦截器
 */
class ReqQueryParamInterceptor : Interceptor {
    /**
     * 查询参数哈希表，临时存放查询参数key-value
     */
    private val queryParamsMap: LinkedHashMap<String, String> = LinkedHashMap()

    /**
     * 添加请求查询参数
     * @param key 字段key
     * @param value 字段值
     */
    fun addQueryParam(key: String, value: String) {
        queryParamsMap[key] = value
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originReq = chain.request()
        val newHttpUrlBuilder = originReq.url.newBuilder()

        queryParamsMap.forEach {
            newHttpUrlBuilder.addQueryParameter(it.key, it.value)
        }


        return chain.proceed(originReq.newBuilder().url(newHttpUrlBuilder.build()).build())
    }
}


fun getBodyJsonStr(body: RequestBody?): String {
    val buffer = Buffer()
    body?.writeTo(buffer)
    var charset = Charset.forName("UTF-8")
    val contentType = body?.contentType()
    if (contentType != null) {
        try {
            charset = contentType.charset(Charset.forName("UTF-8"));
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    return buffer.readString(charset)
}

/**
 * 请求内容拦截器:用于POST 请求 RequestBody 添加公共参数
 */
class BodyInterceptor : Interceptor {

    private val mediaType = "application/json; charset=UTF-8".toMediaTypeOrNull()


    private val bodyParamsMap: java.util.LinkedHashMap<String, Any> = java.util.LinkedHashMap()

    fun addBodyParam(key: String, value: Any) {
        bodyParamsMap[key] = value
    }




    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var originReq = chain.request()


        val bodyJsonStr = getBodyJsonStr(originReq.body)
        val bodyJsonObject = JSONObject(bodyJsonStr)
        val gson = Gson()
        bodyParamsMap.forEach {
            val valueJsonStr = gson.toJson(it.value)
            val valueJsonObject = JSONObject(valueJsonStr)
            bodyJsonObject.put(it.key, valueJsonObject)
        }


        val newReq =
                originReq.newBuilder().post(RequestBody.create(mediaType, bodyJsonObject.toString()))
                        .build()
        return chain.proceed(newReq)


    }
}


/**
 * 请求内容加密拦截器
 */
class ReqEncryptInterceptor : Interceptor {

    private var keyStr: String? = null
    fun readSecretedKeyStr(secretedKeyStr: String) {
        keyStr = secretedKeyStr
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var originReq = chain.request()

        var dataStr = getBodyJsonStr(originReq.body)
        try {
            dataStr = DESUtils.encrypt(dataStr, keyStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val requestBody: RequestBody =
                dataStr.toRequestBody("text/plain; charset=utf-8".toMediaTypeOrNull())
        originReq = originReq.newBuilder()
                .post(requestBody)
                .build()
        return chain.proceed(originReq)
    }
}


/**
 * 启用Gzip压缩
 */
class GzipReqInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originReq = chain.request()
        if (originReq.body == null || originReq.header("Content-Encoding") != null) {
            return chain.proceed(originReq)
        }
        val compressedRequest = originReq.newBuilder()
            .header("Content-Encoding", "gzip")
            .method(originReq.method, gzip(originReq.body!!))
            .build()
        return chain.proceed(compressedRequest)
    }

    private fun gzip(body: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body.contentType()
            }

            @Throws(IOException::class)
            override fun contentLength(): Long {
                return -1
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink: BufferedSink = GzipSink(sink).buffer()
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}

