package com.zjc.insec.insec.executorCoreThread;

import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executor;

/**
 * Created by zjc on 2018/1/18.
 */
public class MonitorRunnable extends Thread{

    public static Logger logger= LogManager.getLogger(MonitorRunnable.class);
    UserRunnable userRunnable;
    FolloweeRunnable followeeRunnable;
    TopicRunnable topicRunnable;
    Executor executor;

    public MonitorRunnable(UserRunnable userRunnable,FolloweeRunnable followeeRunnable,Executor executor){
        this.userRunnable=userRunnable;
        this.followeeRunnable=followeeRunnable;
        this.executor=executor;
    }
    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            if (start - userRunnable.endTime > 60000) {
                userRunnable.httpGet.abort();
                logger.info("userRunnable Timeout interrupted"+followeeRunnable.httpGet.isAborted());
            }
            if (start - followeeRunnable.endTime > 60000) {
                followeeRunnable.httpGet.abort();
                logger.info("followeeRunnable Timeout interrupted"+followeeRunnable.httpGet.isAborted());
            }
            logger.info("monitorRunnable run");
        }catch (Exception e){
            logger.error(e.toString());
        }
    }
}
