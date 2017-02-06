package org.smartx.summer.session;

import java.util.Map;
import java.util.TreeSet;

/**
 * Created by Ming on 2016/11/10.
 */
public interface SessionManager {

    /**
     * 设置 session
     */
    void setSessionUser(String key, SessionUser sessionUser);

    /**
     * 设置 session 中指定的 field
     */
    void setSessionField(String key, String field, String value);

    /**
     * 删除 session 中 指定的 field
     */
    Boolean delSessionField(String key, String field);

    /**
     * 获取 session 中指定 field 的值
     */
    String getSessionField(String key, String field);

    /**
     *
     */
    boolean isExistSessionKey(String key);

    /**
     * 判断指定 session 中是否存在某个 field
     */
    boolean isExistSessionField(String key, String field);

    /**
     * 设置 session 超时的时间
     */
    boolean setSessionExpire(String key, int seconds);

    /**
     * 获取 session user
     */
    Map<String, String> getSessionUser(String key);

    /**
     * 删除 session
     */
    void deleteSessionByKey(String key);

    /**
     * 正则匹配获取 keys
     */
    TreeSet<String> getKeysByPattern(String regrex);

}
