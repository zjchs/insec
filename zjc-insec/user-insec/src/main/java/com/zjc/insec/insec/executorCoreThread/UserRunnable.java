package com.zjc.insec.insec.executorCoreThread;
import com.zjc.insec.insec.db.RedisUntil;
import com.zjc.insec.insec.entity.User;
import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.http.HttpProxy;
import com.zjc.insec.insec.http.UrlProxy;
import com.zjc.insec.insec.until.InsecQueue;
import com.zjc.insec.insec.until.ParseUntil;
import com.zjc.insec.insec.until.StreamUntil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * Created by zjc on 2018/1/10.
 */
public class UserRunnable extends BaseThread{



    public InsecQueue userQueue;

    public InsecQueue topicQueue;

    public InsecQueue followeeQueue;

    public Set history;


    public static Logger logger= LogManager.getLogger(UserRunnable.class);

    public UserRunnable(CloseableHttpClient closeableHttpClient,InsecQueue userQueue,InsecQueue topicQueue,InsecQueue followeeQueue,Set history,HttpProxy httpProxy){
        super(closeableHttpClient,httpProxy);
        this.userQueue=userQueue;
        this.topicQueue=topicQueue;
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
                User user=ParseUntil.parseUser(closeableHttpClient, urlToken, httpGet);
                topicQueue.push(urlToken);
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
