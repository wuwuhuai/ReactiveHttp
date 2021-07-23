package github.leavesc.reactivehttpsamples.core.http

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okio.BufferedSink
import okio.GzipSink
import okio.buffer
import java.io.IOException


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

/**
 * 请求内容加密拦截器
 */
//class RequestEncryptInterceptor : Interceptor {
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originReq = chain.request()
//        val body = originReq.body
//        val buffer = Buffer()
//        body?.writeTo(buffer)
//        var charset= Charset.forName("UTF-8")
//        val contentType = body?.contentType()
//        charset = contentType?.charset(charset)
//        var paramsStr= buffer.readString(charset)
//        try {
//            paramsStr = EncryptUtils.encryptParams(paramsStr)
//        } catch (e: Exception) {
//            LogUtils.e(e)
//        }
//        val requestBody: RequestBody =
//            RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), paramsStr)
//        originReq = originReq.newBuilder()
//            .post(requestBody)
//            .build()
//        return chain.proceed(originReq)
//    }
//}


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

