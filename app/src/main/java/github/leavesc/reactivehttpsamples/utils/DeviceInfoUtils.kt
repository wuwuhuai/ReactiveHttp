package github.leavesc.reactivehttpsamples.utils

import android.content.Context
import android.os.Build
import java.util.*


/**
 * 获取设备宽度（px）
 *
 */
fun getDeviceWidth(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
}

/**
 * 获取设备高度（px）
 */
fun getDeviceHeight(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
}


/**
 * 获取厂商名
 */
fun getDeviceManufacturer(): String? {
    return Build.MANUFACTURER
}

/**
 * 获取产品名
 */
fun getDeviceProduct(): String? {
    return Build.PRODUCT
}

/**
 * 获取手机品牌
 */
fun getDeviceBrand(): String? {
    return Build.BRAND
}

/**
 * 获取手机型号
 */
fun getDeviceModel(): String? {
    return Build.MODEL
}

/**
 * 获取手机主板名
 */
fun getDeviceBoard(): String? {
    return Build.BOARD
}

/**
 * 设备名
 */
fun getDeviceDevice(): String? {
    return Build.DEVICE
}


/**
 * 硬件名
 *
 */
fun getDeviceHardware(): String? {
    return Build.HARDWARE
}

/**
 * 主机
 *
 */
fun getDeviceHost(): String? {
    return Build.HOST
}

/**
 *
 * 显示ID
 */
fun getDeviceDisplay(): String? {
    return Build.DISPLAY
}

/**
 * ID
 *
 */
fun getDeviceId(): String? {
    return Build.ID
}

/**
 * 获取手机用户名
 *
 */
fun getDeviceUser(): String? {
    return Build.USER
}

/**
 * 获取手机Android 系统SDK
 *
 * @return
 */
fun getDeviceSDK(): Int {
    return Build.VERSION.SDK_INT
}

/**
 * 获取手机Android 版本
 *
 * @return
 */
fun getDeviceAndroidVersion(): String? {
    return Build.VERSION.RELEASE
}

/**
 * 获取当前手机系统语言。
 */
fun getDeviceDefaultLanguage(): String {
    return Locale.getDefault().getLanguage()
}

