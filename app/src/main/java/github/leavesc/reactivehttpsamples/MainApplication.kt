package github.leavesc.reactivehttpsamples

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * @Author: leavesC
 * @Date: 2020/10/26 15:30
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
class MainApplication : Application() {
    lateinit var kv: MMKV

    companion object {

        lateinit var app: MainApplication

    }

    override fun onCreate() {
        super.onCreate()
        app = this

        //初始化MMKV
        val rootDir: String = MMKV.initialize(this)
        println("mmkv root: $rootDir")
        kv = MMKV.defaultMMKV()
    }

}