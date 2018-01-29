package com.zjc.insec.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;
import java.util.Queue;

/**
 * Created by zjc on 2018/1/29.
 */
public class KafkaConfig {

    public static  KafkaConsumer<String,Object> kafkaConsumer=null;

    public static Properties config(){
        Properties properties=new Properties();
        properties.put("bootstrap.servers","localhost:9092");
        properties.put("group.id","zjc");
        properties.put("enable.auto.commit","false");
        properties.put("auto.commit.interval.ms","1000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "com.zjc.insec.kafka.KafkaDecoding");
        return properties;
    }
    public static KafkaConsumer<String,Object> getKafkaConsumer(){
        if(kafkaConsumer==null){
            kafkaConsumer=new KafkaConsumer<String, Object>(config());
        }
        return kafkaConsumer;
    }
}
