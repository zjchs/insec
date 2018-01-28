package com.zjc.insec.insec.db;

import com.zjc.insec.insec.until.StreamUntil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisUntil {

    public static JedisPool jedisPool;

    public static Logger logger = LogManager.getLogger(RedisUntil.class);

    public static void set(byte[] key, byte[] value) {
            Jedis jedis = jedisPool.getResource();
            jedis.set(key,value);
    }

    public static byte[] get(byte[] key) {
        byte[] o=null;
        Jedis jedis = jedisPool.getResource();
        o= jedis.get(key);
        return o;
    }
}
