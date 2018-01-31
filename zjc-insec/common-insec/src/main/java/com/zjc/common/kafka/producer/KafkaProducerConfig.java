package com.zjc.common.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

/**
 * Created by zjc on 2018/1/29.
 */
public class KafkaProducerConfig {

    public static Producer<String,Object> kafkaProducer=null;

    public static Properties config(){
        Properties properties=new Properties();
        properties.put("bootstrap.servers","localhost:9092");
        properties.put("acks","all");
        properties.put("retires",0);
        properties.put("linger.ms",1);
        properties.put("buffer.memory",33554422);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "com.zjc.common.kafka.serialize.KafkaEncoding");
        return properties;
    }
    public static Producer<String,Object> getKafkaProducer(){
        if(kafkaProducer==null){
            kafkaProducer=new KafkaProducer<>(config());
        }
        return kafkaProducer;
    }
}
