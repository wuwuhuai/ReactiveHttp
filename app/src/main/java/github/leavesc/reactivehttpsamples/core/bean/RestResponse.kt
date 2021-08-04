package github.leavesc.reactivehttpsamples.core.bean

import com.google.gson.annotations.SerializedName
import github.leavesc.reactivehttp.bean.IHttpWrapBean
import github.leavesc.reactivehttpsamples.core.http.HttpConfig

/**
 * Rest风格响应
 */
class RestResponse<T>(
        @SerializedName("status") var code: Int = 0,
        @SerializedName("info") var message: String? = null,
        @SerializedName("districts") var data: T
) : IHttpWrapBean<T> {

    companion object {

        fun <T> success(data: T): RestResponse<T> {
            return RestResponse(HttpConfig.CODE_SERVER_SUCCESS, "success", data)
        }

        fun <T> failed(data: T): RestResponse<T> {
            return RestResponse(-200, "服务器停止维护了~~", data)
        }

    }

    override val httpCode: Int
        get() = code

    override val httpMsg: String
        get() = message ?: ""

    override val httpData: T
        get() = data

    override val httpIsSuccess: Boolean
        get() = code == HttpConfig.CODE_SERVER_SUCCESS || message == "OK"

    override fun toString(): String {
        return "HttpResBean(code=$code, message=$message, data=$data)"
    }

}