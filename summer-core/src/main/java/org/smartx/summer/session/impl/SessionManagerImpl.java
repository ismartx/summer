package org.smartx.summer.session.impl;

import org.smartx.redis.template.HashRedisTemplate;
import org.smartx.summer.session.SessionAndTokenConstants;
import org.smartx.summer.session.SessionManager;
import org.smartx.summer.session.SessionUser;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Created by Ming on 2016/11/10.
 */
public class SessionManagerImpl implements SessionManager {

    @Resource
    protected HashRedisTemplate hashRedisTemplate;

    //这里仅保存 userId 和 tokenId
    @Override
    public void setSessionUser(String key, SessionUser sessionUser) {
        Map<String, String> sessionMap = new HashMap<>(16);
        sessionMap.put(SessionAndTokenConstants.SESSION_USER_ID, sessionUser.getUid());
        sessionMap.put(SessionAndTokenConstants.SESSION_USER_TOKEN_ID, sessionUser.getJti());
        hashRedisTemplate.hmset(key, sessionMap);
    }

    @Override
    public void setSessionField(String key, String field, String value) {
        hashRedisTemplate.hset(key, field, value);
    }

    @Override
    public Boolean delSessionField(String key, String filed) {
        return hashRedisTemplate.hdel(key, filed);
    }

    @Override
    public String getSessionField(String key, String field) {
        return hashRedisTemplate.hget(key, field);
    }

    @Override
    public boolean isExistSessionKey(String key) {
        return hashRedisTemplate.exists(key);
    }

    @Override
    public boolean isExistSessionField(String key, String field) {
        return hashRedisTemplate.hexists(key, field);
    }

    @Override
    public boolean setSessionExpire(String key, int seconds) {
        return hashRedisTemplate.expire(key, seconds);
    }

    @Override
    public Map<String, String> getSessionUser(String key) {
        return hashRedisTemplate.hgetAll(key);

    }

    @Override
    public void deleteSessionByKey(String key) {
        hashRedisTemplate.del(key);
    }

    /*
    @Override
    public TreeSet<String> getKeysByPattern(String pattern) {
        return hashRedisTemplate.keys(pattern);
    }
    */
}
