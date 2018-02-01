package com.zjc.topic.executorThread;

import com.zjc.common.exception.InsecHttpException;
import com.zjc.common.exception.InsecResultException;
import com.zjc.common.http.until.HttpProxy;
import com.zjc.common.redis.RedisUntil;
import com.zjc.common.until.InsecQueue;
import com.zjc.common.until.ParseUntil;
import com.zjc.common.until.StreamUntil;
import net.sf.json.JSONException;
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

    public InsecQueue offsetQueue;
    public static Logger logger= LogManager.getLogger(TopicRunnable.class);

    public TopicRunnable(CloseableHttpClient closeableHttpClient, InsecQueue topicQueue, HttpProxy httpProxy,InsecQueue offsetQueue){
        super(closeableHttpClient,httpProxy);
        this.topicQueue=topicQueue;
        this.offsetQueue=offsetQueue;
    }

    @Override
    public void run() {
        while(true) {
            String urlToken=topicQueue.pop();
            if("commit".equals(urlToken)){
                String offset=offsetQueue.pop();
                if(offset!=null) {
                    KafkaCounsumerRunnable.iscommit = true;
                    KafkaCounsumerRunnable.currentoffset = Integer.parseInt(offset);
                }
                continue;
            }
            long start = System.currentTimeMillis();
            endTime=start;
            if(urlToken==null){
                continue;
            }
            String proxy= httpProxy.getProxyUrl2();
            this.httpGet=initUrlProxy(proxy);
            try {
                List<String> list= ParseUntil.parseTopics(closeableHttpClient, urlToken, httpGet);
                httpProxy.urlQueue2.add(proxy);
                Map<byte[],byte[]> hash=new HashMap<>();
                hash.put(StreamUntil.serializa(urlToken),StreamUntil.serializeByString(list));
                RedisUntil.hmset(StreamUntil.serializa("topics"),hash);
                long end = System.currentTimeMillis();
                logger.info("topic-urlToken:" + urlToken + "   executeTime:" + (end - start)+"  size="+topicQueue.getSize());
            }catch (InsecResultException e){
                logger.error("add Topic failed:" + e.toString());
            }
            catch (InsecHttpException e) {
                topicQueue.pushFirst(urlToken);
                logger.error("add Topic failed:" + e.toString());
            }catch (Exception e){

            }
        }
    }
}
