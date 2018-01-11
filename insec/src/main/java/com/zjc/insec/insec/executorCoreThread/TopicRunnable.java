package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.exception.TopicException;
import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.http.UrlProxy;
import com.zjc.insec.insec.until.InsecQueue;
import com.zjc.insec.insec.until.ParseUntil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.rmi.runtime.Log;

/**
 * Created by zjc on 2018/1/10.
 */
public class TopicRunnable implements Runnable{


    public CloseableHttpClient closeableHttpClient;

    public InsecQueue topicQueue;

    public static Logger logger= LogManager.getLogger(TopicRunnable.class);

    public TopicRunnable(CloseableHttpClient closeableHttpClient,InsecQueue topicQueue){
        this.closeableHttpClient=closeableHttpClient;
        this.topicQueue=topicQueue;
    }

    @Override
    public void run() {
        while(true) {
            String urlToken=topicQueue.pop();
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
                ParseUntil.parseTopics(closeableHttpClient, urlToken, httpGet);
                long end = System.currentTimeMillis();
                logger.info("topic-urlToken:" + urlToken + "   executeTime:" + (end - start)+"  size="+topicQueue.getSize());
            } catch (Exception e) {
                topicQueue.push(urlToken);
                logger.error("add Topic failed:" + e.toString());
            }
        }
    }
}
