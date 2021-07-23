package github.leavesc.reactivehttpsamples.core.http

import android.widget.Toast
import github.leavesc.monitor.MonitorInterceptor
import github.leavesc.reactivehttp.datasource.RemoteExtendDataSource
import github.leavesc.reactivehttp.viewmodel.IUIActionEvent
import github.leavesc.reactivehttpsamples.MainApplication
import github.leavesc.reactivehttpsamples.core.http.logging.Level
import github.leavesc.reactivehttpsamples.core.http.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @Author: leavesC
 * @Date: 2020/6/23 0:37
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
class SelfRemoteDataSource(iActionEvent: IUIActionEvent?) :
    RemoteExtendDataSource<ApiService>(iActionEvent, ApiService::class.java) {

    companion object {

        private val httpClient: OkHttpClient by lazy {
            createHttpClient()
        }

        private fun createHttpClient(): OkHttpClient {


            /**
             * 日志拦截器:
             * 打印请求和响应
             */
            val loggingInterceptor = LoggingInterceptor.Builder()
                .setLevel(Level.BASIC)
                .log(Platform.WARN)
                .request("Request")
                .response("Response")
                .build()

            /**
             * 请求头拦截器
             */
            val reqHeaderInterceptor = ReqHeaderInterceptor()
            reqHeaderInterceptor.apply {
                addHead("Content-Type", "application/json")
            }


            val builder = OkHttpClient.Builder()
                .readTimeout(1000L, TimeUnit.MILLISECONDS)
                .writeTimeout(1000L, TimeUnit.MILLISECONDS)
                .connectTimeout(1000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(reqHeaderInterceptor)
                .addInterceptor(FilterInterceptor())
                .addInterceptor(MonitorInterceptor(MainApplication.context))
                .addInterceptor(loggingInterceptor)
            return builder.build()
        }

    }

    /**
     * 由子类实现此字段以便获取 baseUrl
     */
    override val baseUrl: String
        get() = HttpConfig.BASE_URL_MAP

    /**
     * 允许子类自己来实现创建 Retrofit 的逻辑
     * 外部无需缓存 Retrofit 实例，ReactiveHttp 内部已做好缓存处理
     * 但外部需要自己判断是否需要对 OKHttpClient 进行缓存
     * @param baseUrl
     */
    override fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun showToast(msg: String) {
        Toast.makeText(MainApplication.context, msg, Toast.LENGTH_SHORT).show()
    }

}