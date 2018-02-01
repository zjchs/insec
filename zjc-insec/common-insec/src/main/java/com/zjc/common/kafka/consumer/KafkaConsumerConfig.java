package com.zjc.common.kafka.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

/**
 * Created by zjc on 2018/1/29.
 */
public class KafkaConsumerConfig {

    public static  KafkaConsumer<String,Object> kafkaConsumer=null;


    public static Properties config(){
        Properties properties=new Properties();
        properties.put("bootstrap.servers","localhost:9093");
        properties.put("group.id","zjc");
        properties.put("auto.offset.reset","earliest");
        properties.put("enable.auto.commit","false");
        properties.put("auto.commit.interval.ms","1000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "com.zjc.common.kafka.serialize.KafkaDecoding");
        return properties;
    }
    public synchronized static KafkaConsumer<String,Object> getKafkaConsumer(){
        if(kafkaConsumer==null){
            kafkaConsumer=new KafkaConsumer<String, Object>(config());
        }
        return kafkaConsumer;
    }
}
