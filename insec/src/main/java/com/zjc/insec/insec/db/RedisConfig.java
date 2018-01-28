package com.zjc.insec.insec.db;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;


@Configuration
public class RedisConfig {

    @Bean
    public JedisPool getJdedisPool(){
        RedisUntil.jedisPool=new JedisPool(genericObjectPoolConfig(),"127.0.0.1",6579);
        return  null;
    }


    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(){
        GenericObjectPoolConfig genericObjectPoolConfig=new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(10);
        genericObjectPoolConfig.setMinIdle(0);
        genericObjectPoolConfig.setMaxTotal(20);
        genericObjectPoolConfig.setMaxWaitMillis(3000);
        return genericObjectPoolConfig;
    }
}
