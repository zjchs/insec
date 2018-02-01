package com.zjc.article.ExecuteThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zjc on 2018/1/18.
 */
public class MonitorRunnable extends Thread{

    public static Logger logger= LogManager.getLogger(MonitorRunnable.class);
    ArticleRunnable articleRunnable;
    KafkaCounsumerRunnable kafkaCounsumerRunnable;
    public MonitorRunnable(ArticleRunnable articleRunnable,KafkaCounsumerRunnable kafkaCounsumerRunnable){
        this.articleRunnable=articleRunnable;
        this.kafkaCounsumerRunnable=kafkaCounsumerRunnable;
    }
    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            if (start - articleRunnable.endTime > 30000) {
                if(articleRunnable.httpGet!=null) {
                    articleRunnable.httpGet.abort();
                }
                logger.info("articleRunnable Timeout interrupted");
            }
            if(start-kafkaCounsumerRunnable.endtime>10000){
                kafkaCounsumerRunnable.kafkaConsumer.wakeup();
                logger.info("KafkaConsumer Timeout interrupted");
            }
            logger.info("monitorRunnable run");
        }catch (Exception e){
            logger.error(e.toString());
        }
    }
}
