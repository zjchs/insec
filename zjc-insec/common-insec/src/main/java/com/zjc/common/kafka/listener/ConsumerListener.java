package com.zjc.common.kafka.listener;

import com.zjc.common.until.InsecQueue;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

/**
 * Created by zjc on 2018/2/2.
 */
public class ConsumerListener implements ConsumerRebalanceListener {

    InsecQueue offsetQueue;

    public static Logger logger= LogManager.getLogger(ConsumerListener.class);

    public ConsumerListener(InsecQueue offsetQueue){
        this.offsetQueue=offsetQueue;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        logger.info("rebanlance start offsetQueue:"+offsetQueue.getSize());
        if(offsetQueue.getSize()!=0){
              offsetQueue.clear();
         }
        logger.info("rebanlance end offsetQueue:"+offsetQueue.getSize());
    }
}
