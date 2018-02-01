package com.zjc.article.ExecuteThread;

import com.zjc.common.entity.Article;
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
 * Created by zjc on 2018/1/29.
 */
public class ArticleRunnable extends BaseThread {

    InsecQueue articleQueue;

    InsecQueue offsetQueue;

    public static Logger logger= LogManager.getLogger(ArticleRunnable.class);

    public ArticleRunnable(CloseableHttpClient closeableHttpClient, HttpProxy httpProxy, InsecQueue articleQueue,InsecQueue offsetQueue){
        super(closeableHttpClient,httpProxy);
        this.articleQueue=articleQueue;
        this.offsetQueue=offsetQueue;
    }
    public void run(){
         while(true){
             String urlToken=articleQueue.pop();
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
             }catch (InsecResultException e){
                 logger.error("add article failed:" + e.toString());
             } catch (InsecHttpException e){
                 articleQueue.pushFirst(urlToken);
                 logger.error("add article failed:" + e.toString());
             }catch (Exception e){

             }
         }
    }
}
