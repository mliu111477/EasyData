package com.easydata.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 对Jedis的封装类。<br />
 *
 * @author Mr.Pro
 */
public class JedisWapper {

    private Jedis jedis;            // redis对象
    private boolean autoRelease;    // 是否自动回收

    /**
     * <p>Constructor for JedisWapper.</p>
     *
     * @param jedis a {@link Jedis} object.
     * @param autoRelease a boolean.
     */
    public JedisWapper(Jedis jedis, boolean autoRelease) {
        this.autoRelease = autoRelease;
        this.jedis = jedis;
    }

    /**
     * <p>releaseFromPool.</p>
     *
     * @param pool a {@link JedisPool} object.
     */
    public void releaseFromPool(JedisPool pool) {
        if (jedis != null && autoRelease) {
            try {
                pool.returnResource(jedis);
            } catch (Exception e) { }
        }
        jedis = null;
    }

    /**
     * <p>Getter for the field <code>jedis</code>.</p>
     *
     * @return a {@link Jedis} object.
     */
    public Jedis getJedis() {
        return jedis;
    }

    /**
     * <p>Setter for the field <code>jedis</code>.</p>
     *
     * @param jedis a {@link Jedis} object.
     */
    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * <p>isAutoRelease.</p>
     *
     * @return a boolean.
     */
    public boolean isAutoRelease() {
        return autoRelease;
    }

    /**
     * <p>Setter for the field <code>autoRelease</code>.</p>
     *
     * @param autoRelease a boolean.
     */
    public void setAutoRelease(boolean autoRelease) {
        this.autoRelease = autoRelease;
    }
}
