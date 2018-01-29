package com.zjc.insec.ExecuteThread;

import com.zjc.insec.db.RedisUntil;
import com.zjc.insec.entity.Article;
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
 * Created by zjc on 2018/1/29.
 */
public class ArticleRunnable extends BaseThread {

    InsecQueue articleQueue;

    public static Logger logger= LogManager.getLogger(ArticleRunnable.class);

    public ArticleRunnable(CloseableHttpClient closeableHttpClient, HttpProxy httpProxy,InsecQueue articleQueue){
        super(closeableHttpClient,httpProxy);
        this.articleQueue=articleQueue;
    }
    public void run(){
         while(true){
             String urlToken=articleQueue.pop();
             if(urlToken==null){
                 continue;
             }
             String proxy=httpProxy.getProxyUrl();
             this.httpGet=this.initUrlProxy(proxy);
             try {
                 long start = System.currentTimeMillis();
                 List<Article> articles = ParseUntil.parsePapers(closeableHttpClient, urlToken, httpGet);
                 httpProxy.urlQueue.add(proxy);
                 Map<byte[],byte[]> hash=new HashMap<>();
                 hash.put(StreamUntil.serializa(urlToken),StreamUntil.serializeByString(articles));
                 RedisUntil.hmset(StreamUntil.serializa("articles"),hash);
                 long end = System.currentTimeMillis();
                 logger.info("topic-urlToken:" + urlToken + "   executeTime:" + (end - start)+"  size="+articleQueue.getSize());
             }catch (Exception e){
                 articleQueue.push(urlToken);
                 logger.error("add Topic failed:" + e.toString());
             }
         }
    }
}
