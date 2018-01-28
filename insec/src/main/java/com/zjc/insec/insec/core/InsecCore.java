package com.zjc.insec.insec.core;



import com.zjc.insec.insec.executorCoreThread.*;
import com.zjc.insec.insec.http.HttpProxy;
import com.zjc.insec.insec.http.UrlProxy;
import com.zjc.insec.insec.until.InitUntil;
import com.zjc.insec.insec.until.InsecQueue;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
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
    public UrlProxy urlProxy;

    @Autowired
    public HttpProxy httpProxy;

    @Autowired
    public CloseableHttpClient closeableHttpClient;

    public void start(){
//        InsecQueue executorQueue=new InsecQueue();
//        HashMap<String,String> history=new HashMap<>();
//        executorQueue.push("ivan-39-18");
//        //Executor executor= Executors.newFixedThreadPool(20);
//        int i=0;
//        List<String> urls=urlProxy.getUrls();
//        List<Integer> port=urlProxy.getPorts();
//        while(true){
//            String urlToken=executorQueue.pop();
//            if(urlToken==null){
//                continue;
//            }
//            if(i>19){
//                i=0;
//                try {
//                  //  Thread.sleep(2000);
//                }catch(Exception e){
//
//                }
//            }
//     //           executor.execute(new InsecRunnable(closeableHttpClient, urls.get(i), port.get(i), executorQueue, history, urlToken,executor));
//            i++;
//        }
////            int i=10;
////            List<String> url=urlProxy.getUrls();
////            List<Integer> port=urlProxy.getPorts();
////                for(;i>0;i--){
////                    executor.execute(new InsecRunnable(closeableHttpClient,url.get(i),port.get(i),executorQueue,history));
////                    if(i==1){
////                        i=10;
////                        try{
////                            Thread.sleep(1000);
////                        }catch (Exception e){
////                            System.out.println(e.toString());
////                        }
////                        System.out.println("ok");
////                    }
////                }
    }

    public void start1(){
        InsecQueue userQueue=new InsecQueue();
        InsecQueue topicQueue=new InsecQueue();
        InsecQueue folowees=new InsecQueue();
        InitUntil.initQueue(userQueue);
        Set<String> history=InitUntil.loadHistorySet();
        Executor executor=Executors.newFixedThreadPool(4);
        ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(2);
        UserRunnable userRunnable= new UserRunnable(closeableHttpClient,userQueue,topicQueue,folowees,history,httpProxy);
        FolloweeRunnable followeeRunnable=new FolloweeRunnable(closeableHttpClient,userQueue,folowees,history,httpProxy);
        executor.execute(userRunnable);
       // executor.execute(new TopicRunnable(closeableHttpClient,topicQueue));
        executor.execute(followeeRunnable);
        executor.execute(new LoadRunnable(userQueue));
        MonitorRunnable monitorRunnable=new MonitorRunnable(userRunnable,followeeRunnable,executor);
        scheduledExecutorService.scheduleAtFixedRate(monitorRunnable,1,1, TimeUnit.MINUTES);
        scheduledExecutorService.scheduleWithFixedDelay(new HistoryRunnable(history),5,5,TimeUnit.MINUTES);
    }

}
