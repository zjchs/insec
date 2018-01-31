package com.zjc.article.ExecuteThread;

import com.zjc.common.entity.Article;
import com.zjc.common.http.until.HttpProxy;
import com.zjc.common.redis.RedisUntil;
import com.zjc.common.until.InsecQueue;
import com.zjc.common.until.ParseUntil;
import com.zjc.common.until.StreamUntil;
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

    public ArticleRunnable(CloseableHttpClient closeableHttpClient, HttpProxy httpProxy, InsecQueue articleQueue){
        super(closeableHttpClient,httpProxy);
        this.articleQueue=articleQueue;
    }
    public void run(){
         while(true){
             String urlToken=articleQueue.pop();
             long start = System.currentTimeMillis();
             endTime=start;
             if(urlToken==null){
                 continue;
             }
             String proxy=httpProxy.getProxyUrl();
             this.httpGet=this.initUrlProxy(proxy);
             try {
                 List<Article> articles = ParseUntil.parsePapers(closeableHttpClient, urlToken, httpGet);
                 httpProxy.urlQueue.add(proxy);
                 Map<byte[],byte[]> hash=new HashMap<>();
                 hash.put(StreamUntil.serializa(urlToken),StreamUntil.serializeByString(articles));
                 RedisUntil.hmset(StreamUntil.serializa("articles"),hash);
                 long end = System.currentTimeMillis();
                 logger.info("article-urlToken:" + urlToken + "   executeTime:" + (end - start)+"  size="+articleQueue.getSize());
             }catch (Exception e){
                 articleQueue.push(urlToken);
                 logger.error("add article failed:" + e.toString());
             }
         }
    }
}
