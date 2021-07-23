package github.leavesc.reactivehttpsamples.core.http

import github.leavesc.reactivehttpsamples.core.bean.DistrictBean
import github.leavesc.reactivehttpsamples.core.bean.ForecastsBean
import github.leavesc.reactivehttpsamples.core.bean.HttpWrapBean
import github.leavesc.reactivehttpsamples.core.bean.param.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @Author: leavesC
 * @Date: 2020/2/25 16:34
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
interface ApiService {

    /**
     * 带body请求使用方法示例
     */
    @POST("config/district")
    suspend fun bodyReqDemo(@Body user: User): HttpWrapBean<List<DistrictBean>>

    @GET("config/district")
    suspend fun getProvince(): HttpWrapBean<List<DistrictBean>>

    @GET("config/district")
    suspend fun getCity(@Query("keywords") keywords: String): HttpWrapBean<List<DistrictBean>>

    @GET("config/district")
    suspend fun getCounty(@Query("keywords") keywords: String): HttpWrapBean<List<DistrictBean>>

    @GET("weather/weatherInfo?extensions=all")
    suspend fun getWeather(@Query("city") city: String): HttpWrapBean<List<ForecastsBean>>

}