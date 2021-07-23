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