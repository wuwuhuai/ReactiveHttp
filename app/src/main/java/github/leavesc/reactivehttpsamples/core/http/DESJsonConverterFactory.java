package github.leavesc.reactivehttpsamples.core.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Create by vinci on 2021/7/25 11:48
 * <p>
 * Description:DES解密并进行json转换
 * usage:
 * .addConverterFactory(DESJsonConverterFactory.create())
 */
public class DESJsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static DESJsonConverterFactory create() {
        return create(new Gson());
    }


    public static DESJsonConverterFactory create(Gson gson) {
        return new DESJsonConverterFactory(gson);
    }


    private DESJsonConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DESJsonResponseBodyConverter(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DESJsonResponseBodyConverter(gson, adapter);
    }
}