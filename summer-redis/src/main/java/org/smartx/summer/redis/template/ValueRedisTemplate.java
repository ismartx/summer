package org.smartx.summer.redis.template;

import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author kext
 * @since summer 0.1
 */
@Component
public class ValueRedisTemplate extends BasicRedisTemplate {
    public ValueRedisTemplate() {
    }

    public void set(String key, String value) {
        this.set(key, (String) value, 0);
    }

    public void set(String key, String value, int ttl) {
        this.jc.set(key, value);
        if (ttl > 0) {
            this.expire(key, ttl);
        }

    }

    public void set(String key, Object value) {
        this.set(key, (Object) value, 0);
    }

    public <T> void set(String key, Object o, int ttl) {
        if (null != o) {
            String value = this.toJsonString(o);
            this.jc.set(key, value);
            if (ttl > 0) {
                this.jc.expire(key, ttl);
            }
        }
    }

    public String setex(String key, int seconds, String value) {
        return this.jc.setex(key, seconds, value);
    }

    public String setex(String key, int seconds, Object value) {
        return this.jc.setex(key, seconds, this.toJsonString(value));
    }

    public boolean setnx(String key, String value, int ttl) {
        Long reply = this.jc.setnx(key, value);
        if (ttl > 0) {
            this.expire(key, ttl);
        }
        return reply == 1L;
    }

    public boolean setnx(String key, String value) {
        return this.setnx(key, (String) value, 0);
    }

    public boolean setnx(String key, Object value) {
        return this.jc.setnx(key, this.toJsonString(value)) == 1L;
    }

    public boolean setnx(String key, Object value, int ttl) {
        Long reply = this.jc.setnx(key, this.toJsonString(value));
        if (ttl > 0) {
            this.expire(key, ttl);
        }
        return reply.longValue() == 1L;
    }

    public String get(String key) {
        return this.jc.get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        return this.fromJson(this.jc.get(key), clazz);
    }

    public Long incr(String key) {
        return this.jc.incr(key);
    }

    public Long decr(String key) {
        return this.jc.decr(key);
    }
}
