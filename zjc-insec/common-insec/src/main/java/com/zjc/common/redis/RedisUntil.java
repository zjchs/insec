package com.zjc.common.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;


public class RedisUntil {

    public static JedisPool jedisPool;

    public static Logger logger = LogManager.getLogger(RedisUntil.class);

    public static void set(byte[] key, byte[] value) {
            Jedis jedis = jedisPool.getResource();
            jedis.set(key,value);
            jedis.close();
    }
    public static byte[] get(byte[] key) {
        byte[] o=null;
        Jedis jedis = jedisPool.getResource();
        o= jedis.get(key);
        jedis.close();
        return o;
    }
    public static void sadd(byte[] key,byte[] value){
        Jedis jedis=jedisPool.getResource();
        jedis.sadd(key,value);
        jedis.close();
    }
    public static Set<byte[]> smembers(byte[] key){
        Jedis jedis=jedisPool.getResource();
        Set<byte[]> set= jedis.smembers(key);
        jedis.close();
        return  set;
    }
    public static void hmset(byte[] key,Map<byte[],byte[]> hash){
        Jedis jedis=jedisPool.getResource();
        jedis.hmset(key,hash);
        jedis.close();
    }
    public static Map<byte[],byte[]> hgetall(byte[] key){

        Jedis jedis=jedisPool.getResource();
        Map<byte[],byte[]> map=jedis.hgetAll(key);
        jedis.close();
        return map;
    }
    public void returnResource(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }
}
