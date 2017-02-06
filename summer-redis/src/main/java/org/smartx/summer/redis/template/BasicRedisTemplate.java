package org.smartx.summer.redis.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.summer.redis.JsonUtils;
import org.smartx.summer.redis.RedisException;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * <p> </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author kext
 * @since summer 0.1
 */
public class BasicRedisTemplate implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(BasicRedisTemplate.class);


    @Resource
    protected JedisCluster jc;

    public BasicRedisTemplate() {
    }

    public void afterPropertiesSet() throws Exception {
        if (null == this.jc) {
            throw new RedisException("Jedis cluster can not be null");
        }
    }

    public boolean del(String key) {
        return this.jc.del(key).longValue() == 1L;
    }

    public Boolean exists(String key) {
        return this.jc.exists(key);
    }

    public Boolean expire(String key, int seconds) {
        return seconds == 0 ? Boolean.valueOf(true) : Boolean.valueOf(this.jc.expire(key, seconds).longValue() == 1L);
    }

    protected String toJsonString(Object o) {
        return JsonUtils.toJsonString(o) != null ? JsonUtils.toJsonString(o) : "";
    }

    protected <T> T fromJson(String json, Class<T> clazz) {
        return json != null && json.length() != 0 ? JsonUtils.parseJson(json, clazz) : null;
    }

    public TreeSet<String> keys(String pattern) {
        logger.debug("Start getting keys...");
        TreeSet<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = jc.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            logger.debug("Getting keys from: {}", k);
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(pattern));
            } catch (Exception e) {
                logger.error("Getting keys error: {}", e);
            } finally {
                logger.debug("Connection closed.");
                connection.close();//用完一定要close这个链接！！！
            }
        }
        logger.debug("Keys gotten!");
        return keys;
    }
}
