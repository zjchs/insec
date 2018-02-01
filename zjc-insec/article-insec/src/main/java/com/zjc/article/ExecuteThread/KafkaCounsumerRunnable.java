package com.zjc.article.ExecuteThread;

import com.zjc.common.kafka.consumer.KafkaConsumerConfig;
import com.zjc.common.until.InsecQueue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by zjc on 2018/1/29.
 */
public class KafkaCounsumerRunnable extends Thread {
    public static Logger logger= LogManager.getLogger(KafkaCounsumerRunnable.class);
    KafkaConsumer<String,Object> kafkaConsumer;
    InsecQueue articleQueue;
    InsecQueue offsetQueue;
    long endtime=System.currentTimeMillis();
    public static long currentoffset=0;
    public static boolean iscommit=false;
    public  KafkaCounsumerRunnable(InsecQueue articleQueue,InsecQueue offsetQueue){
        this.articleQueue=articleQueue;
        this.offsetQueue=offsetQueue;
        kafkaConsumer= KafkaConsumerConfig.getKafkaConsumer();
        kafkaConsumer.subscribe(Arrays.asList("article"));
    }
    public void run(){
       while(true) {
           endtime=System.currentTimeMillis();
           if(iscommit){
               commitOffset();
               iscommit=false;
           }
           try {
               ConsumerRecords<String, Object> consumerRecords = kafkaConsumer.poll(100);
               for (ConsumerRecord consumerRecord : consumerRecords) {
                   articleQueue.pushAll((List) consumerRecord.value());
                   offsetQueue.push(consumerRecord.offset()+"");
                   logger.info("article-kafka-consumer:" + ((List) consumerRecord.value()).size());
               }
           }catch(Exception e){
                logger.error(e.toString());
           }
       }
    }
    public void commitOffset(){
        TopicPartition topicPartition=new TopicPartition("article",0);
        kafkaConsumer.commitSync(Collections.singletonMap(topicPartition,new OffsetAndMetadata(currentoffset+1)));
        System.out.println("ok");
    }
}
