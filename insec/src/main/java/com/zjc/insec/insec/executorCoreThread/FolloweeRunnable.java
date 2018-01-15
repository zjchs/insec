 package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.http.UrlProxy;
import com.zjc.insec.insec.until.InsecQueue;
import com.zjc.insec.insec.until.ParseUntil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Created by zjc on 2018/1/10.
 */
public class FolloweeRunnable implements Runnable {

    public CloseableHttpClient closeableHttpClient;

    public InsecQueue followeeQueue;

    public InsecQueue userQueue;

    public Set history;

    public static Logger logger= LogManager.getLogger(FolloweeRunnable.class);

    public FolloweeRunnable(CloseableHttpClient closeableHttpClient, InsecQueue userQueue, InsecQueue followeeQueue, Set history){
        this.closeableHttpClient=closeableHttpClient;
        this.followeeQueue=followeeQueue;
        this.userQueue=userQueue;
        this.history=history;
    }

    @Override
    public void run() {
        while(true) {
            String urlToken=followeeQueue.pop();
            if(urlToken==null){
                continue;
            }
            String proxy= UrlProxy.getProxy();
            String url=proxy.split(":")[0];
            int port=Integer.parseInt(proxy.split(":")[1]);
            HttpGet httpGet = new HttpGet();
            HttpClientUntil.config(httpGet,url,port);
            try {
                long start = System.currentTimeMillis();
                List<String> followees = ParseUntil.parseFollowees(closeableHttpClient, urlToken, httpGet);
                if(followees!=null) {
                    for (String followee :followees) {
                        if(history.contains(followee)){
                            continue;
                        }
                        userQueue.push(followee);
                    }
                }
                long end = System.currentTimeMillis();
                logger.info("Followee-urlToken:" + urlToken + "   executeTime:" + (end - start)+"  size="+followeeQueue.getSize());
            } catch (Exception e) {
                followeeQueue.push(urlToken);
                logger.error("get Followee failed:" + e.toString());

            }
        }
    }
}
