package org.smartx.summer.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.smartx.summer.bean.CombineRequest;
import org.smartx.summer.bean.Pageable;
import org.smartx.summer.bean.State;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p> json工具类 </p>
 *
 * <b>Creation Time:</b> 2015年3月4日
 *
 * @author kext
 * @since summer 0.1
 */
public final class JsonUtils {

    /**
     * object to json，默认关闭循环引用，不然无法解析同一个list中的同一个引用对象
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * object to json use single quotes
     */
    public static String toJsonStringUseSingleQuotes(Object object) {
        return JSON.toJSONString(object, SerializerFeature.UseSingleQuotes);
    }

    /**
     * date to json, yyyy-MM-dd HH:mm:ss
     */
    public static String toJsonStringForDate(Date date) {
        return JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * json to object
     */
    public static Object parseJson(String json) {
        return JSON.parse(json);
    }

    /**
     * json to map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJsonToMap(
            String json) {
        return (Map<String, Object>) JSON.parse(json);
    }

    /**
     * json to T type
     */
    public static <T> T parseJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * json to list
     */
    public static <T> List<T> parseJsonToList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> pareObjectToMap(Object o) {
        return (Map<String, Object>) JSON.parse(toJsonString(o));
    }

    public static boolean isJsonArray(String json) {
        return json != null && json.trim().startsWith("[");
    }

    public static boolean isJsonObject(String json) {
        return json != null && json.trim().startsWith("{");
    }
}
