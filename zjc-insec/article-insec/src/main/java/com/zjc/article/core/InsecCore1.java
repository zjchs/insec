package com.zjc.article.core;


import com.zjc.article.ExecuteThread.ArticleRunnable;
import com.zjc.article.ExecuteThread.KafkaCounsumerRunnable;
import com.zjc.article.ExecuteThread.MonitorRunnable;
import com.zjc.common.http.until.HttpProxy;
import com.zjc.common.until.InsecQueue;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zjc on 2017/12/29.
 */
@Component
public class InsecCore1 {

    Logger logger= LogManager.getLogger(InsecCore1.class);

    @Autowired
    public HttpProxy httpProxy;

    @Autowired
    public CloseableHttpClient closeableHttpClient;

    public void start(){
         InsecQueue articleQueue=new InsecQueue();
         InsecQueue offsetQueue=new InsecQueue();
         KafkaCounsumerRunnable kafkaCounsumerRunnable=new KafkaCounsumerRunnable(articleQueue,offsetQueue);
         ArticleRunnable articleRunnable=new ArticleRunnable(closeableHttpClient,httpProxy,articleQueue,offsetQueue);
         MonitorRunnable monitorRunnable=new MonitorRunnable(articleRunnable,kafkaCounsumerRunnable);
         Executor executor=Executors.newFixedThreadPool(5);
         executor.execute(kafkaCounsumerRunnable);
         executor.execute(articleRunnable);
         ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(2);
         scheduledExecutorService.scheduleAtFixedRate(monitorRunnable,30,10,TimeUnit.SECONDS);
    }

}
