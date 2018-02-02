package com.zjc.topic.executorThread;

import com.zjc.common.kafka.consumer.KafkaConsumerConfig;
import com.zjc.common.kafka.listener.ConsumerListener;
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
    InsecQueue topicQueue;
    InsecQueue offsetQueue;
    public static long currentoffset=0;
    public static boolean iscommit=false;
    public long endTime=System.currentTimeMillis();

    public KafkaCounsumerRunnable(InsecQueue topicQueue, InsecQueue offsetQueue, ConsumerListener consumerListener){
        this.topicQueue=topicQueue;
        this.offsetQueue=offsetQueue;
        kafkaConsumer= KafkaConsumerConfig.getKafkaConsumer();
        kafkaConsumer.subscribe(Arrays.asList("topic"),consumerListener);
    }
    public void run(){
       while(true) {
           endTime=System.currentTimeMillis();
           if(iscommit){
               commitOffset();
               iscommit=false;
           }
           try {
               ConsumerRecords<String, Object> consumerRecords = kafkaConsumer.poll(100);
               for (ConsumerRecord consumerRecord : consumerRecords) {
                   topicQueue.pushAll((List) consumerRecord.value());
                   offsetQueue.push(consumerRecord.offset()+"");
                   logger.info("article-kafka-consumer:" + ((List) consumerRecord.value()).size()+"  offset="+consumerRecord.offset());
               }
           }catch(Exception e){
                logger.info(e.toString());
           }
       }
    }
    public void commitOffset(){
        TopicPartition topicPartition=new TopicPartition("topic",0);
        kafkaConsumer.commitSync(Collections.singletonMap(topicPartition,new OffsetAndMetadata(currentoffset+1)));
        logger.info("consumer commit offset:"+currentoffset);
    }
}
