package org.smartx.summer.session;

import java.util.Map;

/**
 * Created by Ming on 2016/11/10.
 *
 * @author Ming
 */
public interface SessionManager {

    /**
     * 设置 session
     *
     * @param key
     * @param sessionUser
     */
    void setSessionUser(String key, SessionUser sessionUser);

    /**
     * 设置 session 中指定的 field
     *
     * @param key
     * @param field
     * @param value
     */
    void setSessionField(String key, String field, String value);

    /**
     * 删除 session 中 指定的 field
     *
     * @param key
     * @param field
     */
    Boolean delSessionField(String key, String field);

    /**
     * 获取 session 中指定 field 的值
     *
     * @param key
     * @param field
     */
    String getSessionField(String key, String field);

    /**
     * 判断key是否过期
     *
     * @param key
     */
    boolean isExistSessionKey(String key);

    /**
     * 判断指定 session 中是否存在某个 field
     *
     * @param key
     * @param field
     */
    boolean isExistSessionField(String key, String field);

    /**
     * 设置 session 超时的时间
     *
     * @param key
     * @param seconds
     */
    boolean setSessionExpire(String key, int seconds);

    /**
     * 获取 session user
     *
     * @param key
     */
    Map<String, String> getSessionUser(String key);

    /**
     * 删除 session
     *
     * @param key
     */
    void deleteSessionByKey(String key);

    /*
    /**
     * 正则匹配获取 keys
     */
    //TreeSet<String> getKeysByPattern(String regrex);

}
