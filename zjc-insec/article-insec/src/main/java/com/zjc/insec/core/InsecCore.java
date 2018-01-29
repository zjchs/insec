package com.zjc.insec.core;


import com.zjc.insec.ExecuteThread.ArticleRunnable;
import com.zjc.insec.ExecuteThread.KafkaCounsumerRunnable;
import com.zjc.insec.ExecuteThread.MonitorRunnable;
import com.zjc.insec.ExecuteThread.TopicRunnable;
import com.zjc.insec.http.HttpProxy;
import com.zjc.insec.until.InsecQueue;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zjc on 2017/12/29.
 */
@Component
public class InsecCore {

    Logger logger= LogManager.getLogger(InsecCore.class);

    @Autowired
    public HttpProxy httpProxy;

    @Autowired
    public CloseableHttpClient closeableHttpClient;

    public void start(){
         InsecQueue articleQueue=new InsecQueue();
         InsecQueue topicQueue=new InsecQueue();
         KafkaCounsumerRunnable kafkaCounsumerRunnable=new KafkaCounsumerRunnable(articleQueue,topicQueue);
         ArticleRunnable articleRunnable=new ArticleRunnable(closeableHttpClient,httpProxy,articleQueue);
        TopicRunnable topicRunnable=new TopicRunnable(closeableHttpClient,topicQueue,httpProxy);
        MonitorRunnable monitorRunnable=new MonitorRunnable(articleRunnable,topicRunnable);
        Executor executor=Executors.newFixedThreadPool(5);
        executor.execute(kafkaCounsumerRunnable);
        executor.execute(articleRunnable);
        executor.execute(topicRunnable);
        ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(monitorRunnable,1,1, TimeUnit.MINUTES);
    }

}
