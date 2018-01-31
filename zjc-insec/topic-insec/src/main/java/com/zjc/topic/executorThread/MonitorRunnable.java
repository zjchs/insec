package com.zjc.topic.executorThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zjc on 2018/1/18.
 */
public class MonitorRunnable extends Thread{

    public static Logger logger= LogManager.getLogger(MonitorRunnable.class);
    TopicRunnable topicRunnable;
    public MonitorRunnable(TopicRunnable topicRunnable){
        this.topicRunnable=topicRunnable;
    }
    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            if (start - topicRunnable.endTime > 60000) {
                if(topicRunnable.httpGet!=null) {
                    topicRunnable.httpGet.abort();
                }
                logger.info("topicRunnable Timeout interrupted");
            }
            logger.info("monitorRunnable run");
        }catch (Exception e){
            logger.error(e.toString());
        }
    }
}
