package com.zjc.article.ExecuteThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zjc on 2018/1/18.
 */
public class MonitorRunnable extends Thread{

    public static Logger logger= LogManager.getLogger(MonitorRunnable.class);
    ArticleRunnable articleRunnable;
    public MonitorRunnable(ArticleRunnable articleRunnable){
        this.articleRunnable=articleRunnable;
    }
    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            if (start - articleRunnable.endTime > 60000) {
                if(articleRunnable.httpGet!=null) {
                    articleRunnable.httpGet.abort();
                }
                logger.info("articleRunnable Timeout interrupted");
            }

            logger.info("monitorRunnable run");
        }catch (Exception e){
            logger.error(e.toString());
        }
    }
}
