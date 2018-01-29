package com.zjc.insec.ExecuteThread;

import com.zjc.insec.db.RedisUntil;
import com.zjc.insec.http.HttpProxy;
import com.zjc.insec.until.InsecQueue;
import com.zjc.insec.until.ParseUntil;
import com.zjc.insec.until.StreamUntil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjc on 2018/1/10.
 */
public class TopicRunnable extends BaseThread{


    public InsecQueue topicQueue;


    public static Logger logger= LogManager.getLogger(TopicRunnable.class);

    public TopicRunnable(CloseableHttpClient closeableHttpClient, InsecQueue topicQueue, HttpProxy httpProxy){
        super(closeableHttpClient,httpProxy);
        this.topicQueue=topicQueue;
    }

    @Override
    public void run() {
        while(true) {
            String urlToken=topicQueue.pop();
            if(urlToken==null){
                continue;
            }
            String proxy= httpProxy.getProxyUrl2();
            this.httpGet=initUrlProxy(proxy);
            try {
                long start = System.currentTimeMillis();
                List<String> list= ParseUntil.parseTopics(closeableHttpClient, urlToken, httpGet);
                httpProxy.urlQueue.add(proxy);
                Map<byte[],byte[]> hash=new HashMap<>();
                hash.put(StreamUntil.serializa(urlToken),StreamUntil.serializeByString(list));
                RedisUntil.hmset(StreamUntil.serializa("topics"),hash);
                long end = System.currentTimeMillis();
                logger.info("topic-urlToken:" + urlToken + "   executeTime:" + (end - start)+"  size="+topicQueue.getSize());
            } catch (Exception e) {
                topicQueue.push(urlToken);
                logger.error("add Topic failed:" + e.toString());
            }
        }
    }
}
