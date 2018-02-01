package com.zjc.topic.executorThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zjc on 2018/1/18.
 */
public class MonitorRunnable extends Thread{

    public static Logger logger= LogManager.getLogger(MonitorRunnable.class);
    TopicRunnable topicRunnable;
    KafkaCounsumerRunnable kafkaCounsumerRunnable;
    public MonitorRunnable(TopicRunnable topicRunnable,KafkaCounsumerRunnable kafkaCounsumerRunnable){
        this.topicRunnable=topicRunnable;
        this.kafkaCounsumerRunnable=kafkaCounsumerRunnable;
    }
    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            if (start - topicRunnable.endTime > 30000) {
                if(topicRunnable.httpGet!=null) {
                    topicRunnable.httpGet.abort();
                }
                logger.info("topicRunnable Timeout interrupted");
            }
            if(start-kafkaCounsumerRunnable.endTime>10000){
                kafkaCounsumerRunnable.kafkaConsumer.wakeup();
                logger.info("KafkaConsumer Timeout interrupted");
            }
            logger.info("monitorRunnable run");
        }catch (Exception e){
            logger.error(e.toString());
        }
    }
}
