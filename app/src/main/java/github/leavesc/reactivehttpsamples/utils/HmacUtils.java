package github.leavesc.reactivehttpsamples.utils;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacUtils {
    public static final String TAG = HmacUtils.class.getSimpleName();

    public static String hmacSha256AndBase64(String msg, String key) {
        byte[] hmacBytes = hmacSha256(msg, key);
        //okhttp header需要剥离换行符，不能使用Base64.DEFAULT
        String base64str = Base64.encodeToString(hmacBytes, Base64.NO_WRAP);
        Log.v(TAG, String.format("msg:%s,key:%s,base64str:%s", msg, key, base64str));
        return base64str;
    }

    public static byte[] hmacSha256(String message, String key) {
        try {
            //or "HmacSHA1", "HmacSHA512"
            final String hashingAlgorithm = "HmacSHA256";

            byte[] bytes = hmac(hashingAlgorithm, key.getBytes(), message.getBytes());

            final String messageDigest = bytesToHex(bytes);

            Log.v(TAG, String.format("msg:%s,key:%s,message digest:%s", message, key, messageDigest));

            return bytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }

    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
