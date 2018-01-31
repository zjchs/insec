package com.zjc.insec.insec.executorCoreThread;

import com.zjc.common.entity.User;
import com.zjc.common.http.until.HttpProxy;
import com.zjc.common.redis.RedisUntil;
import com.zjc.common.until.InsecQueue;
import com.zjc.common.until.ParseUntil;
import com.zjc.common.until.StreamUntil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * Created by zjc on 2018/1/10.
 */
public class UserRunnable extends BaseThread{



    public InsecQueue userQueue;

    public InsecQueue articleQueue;

    public InsecQueue followeeQueue;

    public Set history;


    public static Logger logger= LogManager.getLogger(UserRunnable.class);

    public UserRunnable(CloseableHttpClient closeableHttpClient,InsecQueue userQueue,InsecQueue articleQueue,InsecQueue followeeQueue,Set history,HttpProxy httpProxy){
        super(closeableHttpClient,httpProxy);
        this.userQueue=userQueue;
        this.articleQueue=articleQueue;
        this.followeeQueue=followeeQueue;
        this.history=history;
    }
    @Override
    public void run() {
        while(true) {
            String  urlToken=userQueue.pop();
            if(urlToken==null){
                continue;
            }
            if(history.contains(urlToken)){
                continue;
            }
            String proxy=httpProxy.getProxyUrl();
            this.httpGet=initUrlProxy(proxy);
            try {
                long start = System.currentTimeMillis();
                endTime=start;
                User user= ParseUntil.parseUser(closeableHttpClient, urlToken, httpGet);
                articleQueue.push(urlToken);
                followeeQueue.push(urlToken);
                history.add(urlToken);
                RedisUntil.sadd(StreamUntil.serializa("user"),StreamUntil.serializeByString(user));
                httpProxy.urlQueue.add(proxy);
                long end = System.currentTimeMillis();
                logger.info("User-urlToken:" + urlToken + "   executeTime:" + (end - start)+" size="+userQueue.getSize());
            } catch (Exception e) {
                Thread.currentThread().isInterrupted();
                userQueue.push(urlToken);
                logger.error("get User failed:" + e.toString());
            }
        }
    }
}
