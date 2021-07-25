package github.leavesc.reactivehttpsamples.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.util.Base64.DEFAULT;
import static javax.crypto.Cipher.DECRYPT_MODE;

/**
 * Create by vinci on 2021/7/24 11:39
 * <p>
 * Description: Des 加密/解密工具类
 * 用法示例：
 * val data = "林武淮"
 * val secretedKeyStr = "AYA4KcdKEMs"
 * val secretedDataStr = DesUtils.encrypt(data, secretedKeyStr)
 * DesUtils.decrypt(secretedDataStr, secretedKeyStr)
 */
public class DESUtils {

    public static final String TAG = DESUtils.class.getSimpleName();
    public static final String DES_TRANSFORMATION = "DES";

    /**
     * 加密
     *
     * @param data           数据
     * @param secretedKeyStr 秘钥字符串
     * @return 加密后的数据字符串
     */
    public static String encrypt(String data, String secretedKeyStr) {
        if (data == null || secretedKeyStr == null) {
            return null;
        }

        try {
            //创建Cipher实例
            Cipher cipher = Cipher.getInstance(DES_TRANSFORMATION);

            //设置加密模式
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeyFromSecretKeyString(secretedKeyStr));

            //执行加密
            byte[] relBytes = cipher.doFinal(data.getBytes());

            //使用Base64编码 注意：加密过后用Base64编码 缺少这步会导致解密失败
            byte[] relBase = Base64.encode(relBytes, DEFAULT);

            //返回加密后的数据字符串
            String secretedDataStr = new String(relBase);

            Log.v(TAG, String.format("加密：data:%s,secretedKeyStr:%s,secretedDataStr：%s", data, secretedKeyStr, secretedDataStr));
            return secretedDataStr;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     *
     * @param secretedDataStr 已加密了的字符串
     * @param secretedKeyStr  秘钥字符串
     * @return 解密后的数据字符串
     */
    public static String decrypt(String secretedDataStr, String secretedKeyStr) {

        //Des 解码
        try {
            Cipher cipher = Cipher.getInstance(DES_TRANSFORMATION);

            cipher.init(DECRYPT_MODE, getSecretKeyFromSecretKeyString(secretedKeyStr));

            byte[] base64 = Base64.decode(secretedDataStr, DEFAULT);

            String decryptedDataStr = new String(cipher.doFinal(base64));

            Log.v(TAG, String.format("解密：secretedDataStr:%s,secretedKeyStr:%s,decryptedDataStr：%s", secretedDataStr, secretedKeyStr, decryptedDataStr));

            //返回原始数据
            return decryptedDataStr;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从秘钥字符串中获取秘钥
     *
     * @param secretKeyStr 秘钥字符串
     * @return SecretKey
     */
    private static SecretKey getSecretKeyFromSecretKeyString(String secretKeyStr) {
        //将秘钥字符串解码成base64 字节数组
        byte[] decodedKey = Base64.decode(secretKeyStr, DEFAULT);

        //重建 SecretKeySpec

        return new SecretKeySpec(decodedKey, 0, decodedKey.length, DES_TRANSFORMATION);
    }

}
