package com.zjc.topic.executorThread;

import com.zjc.common.kafka.consumer.KafkaConsumerConfig;
import com.zjc.common.until.InsecQueue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zjc on 2018/1/29.
 */
public class KafkaCounsumerRunnable extends Thread {
    public static Logger logger= LogManager.getLogger(KafkaCounsumerRunnable.class);
    KafkaConsumer<String,Object> kafkaConsumer;
    InsecQueue topicQueue;

    public KafkaCounsumerRunnable(InsecQueue topicQueue){
        this.topicQueue=topicQueue;
        kafkaConsumer= KafkaConsumerConfig.getKafkaConsumer();
        kafkaConsumer.subscribe(Arrays.asList("topic"));
    }
    public void run(){
       while(true) {
           try {
               ConsumerRecords<String, Object> consumerRecords = kafkaConsumer.poll(100);
               for (ConsumerRecord consumerRecord : consumerRecords) {
                   topicQueue.pushAll((List) consumerRecord.value());
                   logger.info("article-kafka-consumer:" + ((List) consumerRecord.value()).size());
               }
               kafkaConsumer.commitSync();
           }catch(Exception e){
                logger.error(e.toString());
           }
       }
    }
}
