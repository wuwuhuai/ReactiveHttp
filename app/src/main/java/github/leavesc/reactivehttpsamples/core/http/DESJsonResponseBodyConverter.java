package github.leavesc.reactivehttpsamples.core.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import github.leavesc.reactivehttpsamples.utils.DESUtils;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Create by vinci on 2021/7/25 11:51
 * <p>
 * Description: DES解密并进行json转换
 */
public class DESJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    public static final String TAG = DESJsonResponseBodyConverter.class.getSimpleName();
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public DESJsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {

        //加密的json字符串
        String secretedDataStr = responseBody.string();

        //解密
        String decryptString = DESUtils.decrypt(secretedDataStr, "AYA4KcdKEMs");

        //对解密的字符串进行处理
        int position = decryptString.lastIndexOf("}");
        String jsonString = decryptString.substring(0, position + 1);


        Log.v(TAG, "后端响应数据字符串：" + secretedDataStr);
        Log.v(TAG, "解密后的数据字符串：" + decryptString);
        Log.v(TAG, "解密后的数据json字符串：" + jsonString);

        //这部分代码参考GsonConverterFactory中GsonResponseBodyConverter<T>的源码对json的处理
        Reader reader = stringToReader(jsonString);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            reader.close();
            jsonReader.close();
        }
    }

    /**
     * String转Reader
     *
     * @param json
     * @return
     */
    private Reader stringToReader(String json) {
        Reader reader = new StringReader(json);
        return reader;
    }

    /**
     * 模拟后端响应，用来测试这个转换器是否正常工作
     *
     * @return
     */
    private String mockResponse() {

        //未加密json字符串
        String rawJsonStr = " {\n" +
                "\t\"name\": \"林武淮\"\n" +
                "}";
        String secretedKeyStr = "AYA4KcdKEMs";
        return DESUtils.encrypt(rawJsonStr, secretedKeyStr);
    }

}
