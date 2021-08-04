package github.leavesc.reactivehttpsamples.core.http

import github.leavesc.reactivehttpsamples.core.bean.DistrictBean
import github.leavesc.reactivehttpsamples.core.bean.RestResponse
import github.leavesc.reactivehttpsamples.core.bean.param.Data
import retrofit2.http.GET

/**
 * @Author: leavesC
 * @Date: 2020/2/25 16:34
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
interface ApiService {

    @GET("config/district")
    suspend fun getProvince(): Data

    @GET("config/district")
    suspend fun restGetProvince(): RestResponse<List<DistrictBean>>

}