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

    @GET("config/district")
    suspend fun getProvince(): HttpWrapBean<List<DistrictBean>>

}