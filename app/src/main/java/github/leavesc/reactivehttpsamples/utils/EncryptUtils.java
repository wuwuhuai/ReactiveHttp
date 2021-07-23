package github.leavesc.reactivehttpsamples.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptUtils {

    /**
     * dex加密
     */
    public static String encrypt(String transformation, String data,) {
        if (data == null) {
            return null;
        }

        try {
            //1，创建Cipher实例
            Cipher cipher = Cipher.getInstance(transformation);
            //2，创建秘钥
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            //3，设置加密模式
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //4，执行加密
            byte[] relBytes = cipher.doFinal(data.getBytes());

            //5，使用Base64编码 注意：加密过后用Base64编码 缺少这步会导致解密失败
            byte[] relBase = Base64.encode(relBytes, Base64.DEFAULT);

            //6，返回加密后的字符串
            String encodeStr = new String(relBase);

            Log.d("xl", encodeStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
