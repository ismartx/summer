package org.smartx.summer.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashSet;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * <p>  </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author kext
 * @since summer 0.1
 */
public class JedisClusterConnectionFactoryBean implements FactoryBean<JedisCluster>, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisClusterConnectionFactoryBean.class);
    private String servers;
    private int timeout = 3000;
    private int maxRedirections = 3;

    private GenericObjectPoolConfig poolConfig;
    private JedisCluster jc;

    public JedisClusterConnectionFactoryBean() {
    }

    public JedisCluster getObject() throws Exception {
        return this.jc;
    }

    public Class<?> getObjectType() {
        return this.jc != null ? this.jc.getClass() : JedisCluster.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        if (null == this.poolConfig) {
            throw new RedisException("Pool config cant not be null");
        } else {
            HashSet jedisClusterNodes = new HashSet();
            String[] var2 = this.servers.split("[,]");
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String server = var2[var4];
                String[] sa = server.split("[:]");
                if (sa.length == 2) {
                    String host = sa[0];
                    int port = Integer.parseInt(sa[1]);
                    jedisClusterNodes.add(new HostAndPort(host, port));
                }
            }
            this.jc = new JedisCluster(jedisClusterNodes, this.timeout, this.maxRedirections, this.poolConfig);
        }
    }

    public void destroy() throws Exception {
        if (this.jc != null) {
            try {
                this.jc.close();
            } catch (Exception var2) {
                logger.warn("Can not close Jedis cluster", var2);
            }
            this.jc = null;
        }

    }

    public String getServers() {
        return this.servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxRedirections() {
        return this.maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return this.poolConfig;
    }

    public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }
}
