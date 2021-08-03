package github.leavesc.reactivehttp.datasource

import github.leavesc.reactivehttp.callback.RequestCallback
import github.leavesc.reactivehttp.viewmodel.IUIActionEvent
import kotlinx.coroutines.Job

/**
 * @Author: leavesC
 * @Date: 2020/5/4 0:55
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
abstract class RemoteDataSource<Api : Any>(
    iUiActionEvent: IUIActionEvent?,
    apiServiceClass: Class<Api>
) : BaseRemoteDataSource<Api>(iUiActionEvent, apiServiceClass) {

    fun <Data> enqueueLoading(
        apiFun: suspend Api.() -> Data,
        baseUrl: String = "",
        callbackFun: (RequestCallback<Data>.() -> Unit)? = null
    ): Job {
        return enqueue(
            apiFun = apiFun,
            showLoading = true,
            baseUrl = baseUrl,
            callbackFun = callbackFun
        )
    }

    fun <Data> enqueue(
        apiFun: suspend Api.() -> Data,
        showLoading: Boolean = false,
        baseUrl: String = "",
        callbackFun: (RequestCallback<Data>.() -> Unit)? = null
    ): Job {
        return launchMain {
            val callback = if (callbackFun == null) {
                null
            } else {
                RequestCallback<Data>().apply {
                    callbackFun.invoke(this)
                }
            }
            try {
                if (showLoading) {
                    showLoading(coroutineContext[Job])
                }
                callback?.onStart?.invoke()
                val response: Data
                try {
                    response = apiFun.invoke(getApiService(baseUrl))
//                    if (!response.httpIsSuccess) {
//                        throw ServerCodeBadException(response)
//                    }
                } catch (throwable: Throwable) {
                    handleException(throwable, callback)
                    return@launchMain
                }
                onGetResponse(callback, response)
            } finally {
                try {
                    callback?.onFinally?.invoke()
                } finally {
                    if (showLoading) {
                        dismissLoading()
                    }
                }
            }
        }
    }


    private suspend fun <Data> onGetResponse(callback: RequestCallback<Data>?, httpData: Data) {
        callback?.let {
            withNonCancellable {
                callback.onSuccess?.let {
                    withMain {
                        it.invoke(httpData)
                    }
                }
                callback.onSuccessIO?.let {
                    withIO {
                        it.invoke(httpData)
                    }
                }
            }
        }
    }


}