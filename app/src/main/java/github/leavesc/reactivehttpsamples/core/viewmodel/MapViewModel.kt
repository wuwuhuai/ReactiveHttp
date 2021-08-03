package github.leavesc.reactivehttpsamples.core.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import github.leavesc.reactivehttpsamples.base.BaseViewModel
import github.leavesc.reactivehttpsamples.core.bean.DistrictBean
import kotlinx.coroutines.delay

/**
 * @Author: leavesC
 * @Date: 2020/6/23 0:37
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
class MapViewModel : BaseViewModel() {

    companion object {

        const val TYPE_PROVINCE = 10

        const val TYPE_CITY = 20

        const val TYPE_COUNTY = 30

    }

    val stateLiveData = MutableLiveData<Int>()

    init {
        stateLiveData.value = TYPE_PROVINCE
    }

    private val provinceLiveData = MutableLiveData<List<DistrictBean>>()

    private val cityLiveData = MutableLiveData<List<DistrictBean>>()

    val realLiveData = MutableLiveData<List<DistrictBean>>()

    val adCodeSelectedLiveData = MutableLiveData<String>()


    fun getProvince() {
        remoteDataSource.enqueueLoading({
            //主动延迟一段时间，避免弹窗太快消失
            delay(2000)
            getProvince()
        }) {
            onStart {
                log("onStart")
            }
            onSuccess {
                log("onSuccess")
                stateLiveData.value = TYPE_PROVINCE
                provinceLiveData.value = it.districts[0].districts
                realLiveData.value = it.districts[0].districts
            }
            onSuccessIO {
                log("onSuccessIO")
            }
            onFailed {
                log("onFailed")
            }
            onCancelled {
                log("onCancelled")
            }
            onFinally {
                log("onFinally")
            }
        }
    }

    fun onBackPressed(): Boolean {
        when (stateLiveData.value) {
            TYPE_PROVINCE -> {
                return true
            }
            TYPE_CITY -> {
                stateLiveData.value = TYPE_PROVINCE
                realLiveData.value = provinceLiveData.value
            }
            TYPE_COUNTY -> {
                stateLiveData.value = TYPE_CITY
                realLiveData.value = cityLiveData.value
            }
        }
        return false
    }

    fun onPlaceClicked(position: Int) {
        when (stateLiveData.value) {
        }
    }

    private var log = ""

    @Synchronized
    private fun log(msg: String) {
        val newLog = "[${Thread.currentThread().name}]-${msg}"
        log = log + "\n" + newLog
        Log.e("TAG", newLog)
    }

}