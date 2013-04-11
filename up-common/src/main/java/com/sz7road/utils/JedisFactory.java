package com.sz7road.utils;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {

    private static JedisPool jedisPool;

    public JedisFactory() {
        super();
    }

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(100);
        config.setMaxIdle(5000);
        config.setMaxWait(5000);
        config.setTestOnBorrow(true);
        String jedisCacheIP = ConfigurationUtils.get("jedisCacheIP");
        jedisPool = new JedisPool(config, "10.10.3.168", 6379, 5000); //本地
//        jedisPool = new JedisPool(config, "127.0.0.1", 6379, 5000);   //测试服
//        jedisPool = new JedisPool(config, "192.168.1.10", 6379, 5000);   //正式服
    }

    public Jedis getJedisInstance() {
        Jedis jedis = jedisPool.getResource();
//        jedis.auth("love7roadredis!");
        return jedis;
    }

    /**
     * 配合使用getJedisInstance方法后将jedis对象释放回连接池中
     *
     * @param jedis 使用完毕的Jedis对象
     * @return true 释放成功；否则返回false
     */
    public boolean release(Jedis jedis) {
        if (jedisPool != null && jedis != null) {
            jedisPool.returnResource(jedis);
            return true;
        }
        return false;
    }

}  