 package com.zjc.insec.insec.executorCoreThread;

import com.zjc.common.http.until.HttpProxy;
import com.zjc.common.until.InsecQueue;
import com.zjc.common.until.ParseUntil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Created by zjc on 2018/1/10.
 */
public class FolloweeRunnable extends BaseThread {

    public InsecQueue followeeQueue;

    public InsecQueue userQueue;

    public Set history;

    public static Logger logger= LogManager.getLogger(FolloweeRunnable.class);

    public FolloweeRunnable(CloseableHttpClient closeableHttpClient, InsecQueue userQueue, InsecQueue followeeQueue, Set history,HttpProxy httpProxy){
        super(closeableHttpClient,httpProxy);
        this.closeableHttpClient=closeableHttpClient;
        this.followeeQueue=followeeQueue;
        this.userQueue=userQueue;
        this.history=history;
        this.httpProxy=httpProxy;
    }

    @Override
    public void run() {
        while(true) {
            String urlToken=followeeQueue.pop();
            if(urlToken==null){
                continue;
            }
            String proxy= httpProxy.getProxyUrl1();
            this.httpGet=initUrlProxy(proxy);
            try {
                long start = System.currentTimeMillis();
                endTime=start;
                List<String> followees = ParseUntil.parseFollowees(closeableHttpClient, urlToken, httpGet);
                if(followees!=null) {
                    for (String followee :followees) {
                        if(history.contains(followee)){
                            continue;
                        }
                        userQueue.push(followee);
                    }
                }
                httpProxy.urlQueue1.add(proxy);
                long end = System.currentTimeMillis();
                logger.info("Followee-urlToken:" + urlToken + "   executeTime:" + (end - start)+"  size="+followeeQueue.getSize());
            } catch (Exception e) {
                Thread.currentThread().isInterrupted();
                followeeQueue.push(urlToken);
                logger.error("get Followee failed:" + e.toString());
            }
        }
    }
}
