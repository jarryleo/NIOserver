package cn.leo.nio.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtil {
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            Logger.d(json);
            return JSON.parseObject(json, classOfT);
            //return new Gson().fromJson(json, classOfT);

        } catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Object src) {
        return JSON.toJSONString(src);
        //return new Gson().toJson(src);
    }
}
