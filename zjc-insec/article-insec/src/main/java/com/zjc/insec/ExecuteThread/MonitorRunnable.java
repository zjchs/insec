package com.zjc.insec.ExecuteThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executor;

/**
 * Created by zjc on 2018/1/18.
 */
public class MonitorRunnable extends Thread{

    public static Logger logger= LogManager.getLogger(MonitorRunnable.class);
    ArticleRunnable articleRunnable;
    TopicRunnable topicRunnable;
    public MonitorRunnable(ArticleRunnable articleRunnable,TopicRunnable topicRunnable){
        this.articleRunnable=articleRunnable;
        this.topicRunnable=topicRunnable;
    }
    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            if (start - articleRunnable.endTime > 120000) {
                articleRunnable.httpGet.abort();
                logger.info("articleRunnable Timeout interrupted"+articleRunnable.httpGet.isAborted());
            }
            if (start - topicRunnable.endTime > 120000) {
                topicRunnable.httpGet.abort();
                logger.info("topicRunnable Timeout interrupted"+topicRunnable.httpGet.isAborted());
            }
            logger.info("monitorRunnable run");
        }catch (Exception e){
            logger.error(e.toString());
        }
    }
}
