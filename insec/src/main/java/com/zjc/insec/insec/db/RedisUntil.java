package com.zjc.insec.insec.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

@Component
public class RedisUntil {
    @Autowired
    JedisPool jedisPool;
}
