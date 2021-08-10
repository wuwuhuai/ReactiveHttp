package github.leavesc.reactivehttpsamples.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import github.leavesc.reactivehttp.base.BaseReactiveActivity
import github.leavesc.reactivehttpsamples.MainApplication
import github.leavesc.reactivehttpsamples.R
import github.leavesc.reactivehttpsamples.constant.MMKVConst
import github.leavesc.reactivehttpsamples.utils.*
import github.leavesc.reactivehttpsamples.utils.PermissionRequestCodeConst.Companion.REQUEST_CODE_LOCATION

/**
 * @Author: leavesC
 * @Date: 2020/10/22 10:29
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
abstract class BaseActivity : BaseReactiveActivity() {
    /**
     * 位置权限
     */
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    protected inline fun <reified T : Activity> startActivity() {
        lContext?.apply {
            startActivity(Intent(this, T::class.java))
        }
    }


    /**
     * 请求位置
     */
    fun requestLocation() {
        if (hasPermissions(locationPermission)) {
            requestLocationService()
        } else {
            requestPermissions(
                locationPermission,
                requestCode = REQUEST_CODE_LOCATION
            )
        }

    }

    private fun requestLocationService() {
        if (LocationServiceEnable()) {
            Toast.makeText(this, "已开启定位服务", Toast.LENGTH_SHORT).show()
            // TODO: 2021/8/10 启动定位，例如google地图定位
        } else {
            Toast.makeText(this, "没有开启定位服务", Toast.LENGTH_SHORT).show()
            startActivity(LocationSettingIntent())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MainApplication.app.kv.remove(MMKVConst.KEY_REFUSED_LOCATION_PERMISSION)
                    Toast.makeText(this, "已经获得权限，执行下一步操作", Toast.LENGTH_SHORT).show()
                    requestLocationService()
                } else {
                    MainApplication.app.kv.encode(MMKVConst.KEY_REFUSED_LOCATION_PERMISSION, true)
                    Toast.makeText(this, "用户拒绝授予权限", Toast.LENGTH_SHORT).show()
                    //  显示弹窗
//            binding.llMockDialog.visibility = View.VISIBLE
//            binding.llMockDialog.let {
//                it.close2.setOnClickListener {
//                    binding.llMockDialog.visibility = View.GONE
//                }
//                it.turn_on2.setOnClickListener {
//                    binding.llMockDialog.visibility = View.GONE
//                    requestPermissions(locationPermission, requestCode = PermissionRequestCodeConst.REQUEST_CODE_LOCATION)
//                }
//            }
                }

            }


        }
    }

    /**
     * 空接口，交由需要定位服务的子类实现，获取定位结果
     */
    open fun requestLocationSuccess() {}

}