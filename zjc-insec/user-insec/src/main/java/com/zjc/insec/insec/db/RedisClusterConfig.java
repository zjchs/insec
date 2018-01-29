package com.zjc.insec.insec.db;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisClusterConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    @Bean
    public JedisCluster getJedisCluster(){
        Set<HostAndPort> hostAndPorts=new HashSet<>();
        String[] ports=port.split(",");
        for(String p:ports){
            HostAndPort hostAndPort=new HostAndPort("host",Integer.parseInt(p));
            hostAndPorts.add(hostAndPort);
        }
        return new JedisCluster(hostAndPorts,genericObjectPoolConfig());
    }

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(){
        GenericObjectPoolConfig genericObjectPoolConfig=new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(20);
        genericObjectPoolConfig.setMinIdle(0);
        genericObjectPoolConfig.setMaxTotal(20);
        genericObjectPoolConfig.setMaxWaitMillis(3000);
        return genericObjectPoolConfig;
    }

    @Bean
    public Jedis getJedis(){
        Jedis jedis=new Jedis("127.0.0.1",6379);
        return  jedis;
    }

}
