package github.leavesc.reactivehttpsamples.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.Size
import androidx.core.content.ContextCompat

/**
 * 判断是否有权限
 */
fun Context.hasPermissions(@Size(min = 1) vararg permission: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
    return permission.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * 申请权限（如果是Fragment 则可新增方法 Fragment.requestPermissions）
 */
fun Activity.requestPermissions(@Size(min = 1) vararg permissions: String, requestCode: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
    this.requestPermissions(permissions, requestCode)
}

/**
 * 位置权限申请框 勾选禁止后不再询问 是否弹出自己的提示框
 */
fun Activity.shouldShowCustomPermissionRequestHint(permission: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
    return !shouldShowRequestPermissionRationale(permission)
}

/**
 * 判断是否打开了定位服务
 */
fun Context.LocationServiceEnable(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

/**
 * 跳转App设置界面
 */
fun Context.appSettingIntent(): Intent {
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:$packageName")
    }
}

/**
 * 跳转系统 位置服务 设置界面
 */
fun LocationSettingIntent(): Intent {
    return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
}

/**
 * 统一管理权限请求代码常量
 */
interface PermissionRequestCodeConst {
    companion object {

        /**
         * 位置权限代码
         */
        const val REQUEST_CODE_LOCATION = 1


    }
}