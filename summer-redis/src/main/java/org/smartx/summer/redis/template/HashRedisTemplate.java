package org.smartx.summer.redis.template;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.valueOf;

/**
 * <p>  </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author kext
 * @since summer 0.1
 */
@Component
public class HashRedisTemplate extends BasicRedisTemplate {

    public HashRedisTemplate() {
    }

    public Boolean hset(String key, String field, String value) {
        return this.hset(key, field, value, 0);
    }

    public Boolean hset(String key, String field, String value, int ttl) {
        Long reply = this.jc.hset(key, field, value);
        if (ttl > 0) {
            this.jc.expire(key, ttl);
        }
        return valueOf(reply == 1L);
    }

    public Boolean hset(String key, String field, Object value) {
        return this.hset(key, field, value, 0);
    }

    public Boolean hset(String key, String field, Object value, int ttl) {
        Long reply = this.jc.hset(key, field, this.toJsonString(value));
        if (ttl > 0) {
            this.jc.expire(key, ttl);
        }

        return valueOf(reply == 1L);
    }

    public String hget(String key, String field) {
        return this.hget(key, field, 0);
    }

    public String hget(String key, String field, int ttl) {
        String res = this.jc.hget(key, field);
        if (ttl > 0) {
            this.expire(key, ttl);
        }

        return res;
    }

    public <T> T hget(String key, String field, Class<T> clazz) {
        return this.hget(key, field, clazz, 0);
    }

    public <T> T hget(String key, String field, Class<T> clazz, int ttl) {
        Object res = this.fromJson(this.jc.hget(key, field), clazz);
        if (ttl > 0) {
            this.expire(key, ttl);
        }

        return (T) res;
    }

    public Boolean hdel(String key, String... fields) {
        return valueOf(this.jc.hdel(key, fields) == 1L);
    }

    public Boolean hexists(String key, String field) {
        return this.jc.hexists(key, field);
    }

    public Map<String, String> hgetAll(String key) {
        return this.jc.hgetAll(key);
    }

    public <T> Map<String, T> hgetAllObject(String key, Class<T> clazz) {
        Map byteMap = this.jc.hgetAll(key);
        if (byteMap != null && byteMap.size() > 0) {
            HashMap map = new HashMap();
            Iterator var5 = byteMap.entrySet().iterator();

            while (var5.hasNext()) {
                Map.Entry e = (Map.Entry) var5.next();
                map.put(new String((String) e.getKey()), this.fromJson((String) e.getValue(), clazz));
            }

            return map;
        } else {
            return null;
        }
    }

    public List<String> hmget(String key, String... fields) {
        return this.jc.hmget(key, fields);
    }

    public Map<String, Object> hmgetObject(String key, int ttl, String... fields) {
        if (null == fields) {
            return null;
        } else {
            List res = this.jc.hmget(key, fields);
            HashMap resMap = null;
            if (null != res) {
                resMap = new HashMap();

                for (int i = 0; i < res.size(); ++i) {
                    resMap.put(fields[i], this.fromJson((String) res.get(i), Object.class));
                }
            }

            if (ttl > 0) {
                this.expire(key, ttl);
            }
            return resMap;
        }
    }

    public Map<String, Object> hmgetObject(String key, String... fields) {
        return this.hmgetObject(key, 0, fields);
    }

    public String hmset(String key, Map<String, String> hash) {
        return this.jc.hmset(key, hash);
    }

    public Long hincrBy(String key, String field, long value) {
        return this.jc.hincrBy(key, field, value);
    }

    public Set<String> hkeys(String key) {
        return this.jc.hkeys(key);
    }

    public <T> Set<T> hkeys(String key, Class<T> clazz) {
        Set set = this.jc.hkeys(key);
        HashSet objectSet = new HashSet();
        if (set != null && set.size() != 0) {
            Iterator var5 = set.iterator();

            while (var5.hasNext()) {
                String b = (String) var5.next();
                objectSet.add(this.fromJson(b, clazz));
            }
        }

        return objectSet;
    }

    public Long hlen(String key) {
        return this.jc.hlen(key);
    }
}
