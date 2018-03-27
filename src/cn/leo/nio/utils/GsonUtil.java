package cn.leo.nio.utils;

import com.google.gson.Gson;

public class GsonUtil {
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            Logger.d(json);
            return new Gson().fromJson(json, classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Object src) {
        return new Gson().toJson(src);
    }
}
