package com.zjc.topic.core;


import com.zjc.common.http.until.HttpProxy;
import com.zjc.common.until.InitUntil;
import com.zjc.common.until.InsecQueue;
import com.zjc.topic.executorThread.KafkaCounsumerRunnable;
import com.zjc.topic.executorThread.MonitorRunnable;
import com.zjc.topic.executorThread.TopicRunnable;
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
public class InsecCore {

    Logger logger= LogManager.getLogger(InsecCore.class);


    @Autowired
    public HttpProxy httpProxy;

    @Autowired
    public CloseableHttpClient closeableHttpClient;

    public void start(){
         InsecQueue topicQueue=new InsecQueue();
         InsecQueue offsetQueue=new InsecQueue();
         Executor executor= Executors.newFixedThreadPool(3);
         ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(2);
         KafkaCounsumerRunnable kafkaCounsumerRunnable=new KafkaCounsumerRunnable(topicQueue,offsetQueue);
         TopicRunnable topicRunnable=new TopicRunnable(closeableHttpClient,topicQueue,httpProxy,offsetQueue);
         MonitorRunnable monitorRunnable=new MonitorRunnable(topicRunnable,kafkaCounsumerRunnable);
         executor.execute(kafkaCounsumerRunnable);
         executor.execute(topicRunnable);
         scheduledExecutorService.scheduleAtFixedRate(monitorRunnable,30,10, TimeUnit.SECONDS);
    }

}
