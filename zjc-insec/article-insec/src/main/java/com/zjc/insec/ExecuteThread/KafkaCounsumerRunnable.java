package com.zjc.insec.ExecuteThread;

import com.zjc.insec.kafka.KafkaConfig;
import com.zjc.insec.until.InsecQueue;
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
    InsecQueue articleQueue;
    InsecQueue topicQueue;

    public  KafkaCounsumerRunnable(InsecQueue articleQueue,InsecQueue topicQueue){
        this.articleQueue=articleQueue;
        this.topicQueue=topicQueue;
        kafkaConsumer= KafkaConfig.getKafkaConsumer();
        kafkaConsumer.subscribe(Arrays.asList("articles"));
    }
    public void run(){
       while(true){
           ConsumerRecords<String,Object> consumerRecords=kafkaConsumer.poll(100);
           for(ConsumerRecord consumerRecord:consumerRecords){
               articleQueue.pushAll((List)consumerRecord.value());
               topicQueue.pushAll((List)consumerRecord.value());
               logger.info("article-kafka-consumer:"+((List) consumerRecord.value()).size());
           }
           kafkaConsumer.commitSync();
       }
    }
}
