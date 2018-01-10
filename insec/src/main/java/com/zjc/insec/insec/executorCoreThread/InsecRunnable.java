package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.entity.article;
import com.zjc.insec.insec.entity.user;
import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.until.InsecQueue;
import com.zjc.insec.insec.until.ParseUntil;


import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

public class InsecRunnable implements Runnable {


    public CloseableHttpClient closeableHttpClient;

    public String proxyUrl;

    public int proxyPort;

    public InsecQueue executorQueue;

    public HashMap<String,String> history;

    public String urlToken;

    public Executor executor;

    Logger logger= LogManager.getLogger(InsecRunnable.class);

    public InsecRunnable(CloseableHttpClient closeableHttpClient, String proxyUrl, int proxyPort, InsecQueue executorQueue, HashMap<String,String > history,String urlToken,Executor executor){
        this.closeableHttpClient=closeableHttpClient;
        this.proxyUrl=proxyUrl;
        this.proxyPort=proxyPort;
        this.executorQueue=executorQueue;
        this.history=history;
        this.urlToken=urlToken;
        this.executor=executor;
    }

    @Override
    public void run(){
        HttpGet httpGet=new HttpGet(this.urlToken);
        HttpClientUntil.config(httpGet,this.proxyUrl,this.proxyPort);
        try{
            long start=System.currentTimeMillis();
                //获取个人信息主体
                user user=ParseUntil.parseUser(closeableHttpClient,urlToken,httpGet);

                //获取用户关注的话题
                user.setTopics(ParseUntil.parseTopics(closeableHttpClient,urlToken,httpGet));

                //获取关注者
                List<String> followees =ParseUntil.parseFollowees(closeableHttpClient,urlToken,httpGet);
                for(String followee:followees){
                    executorQueue.push(followee);
                }
                //获取Paper
                List<article> papers=ParseUntil.parsePapers(closeableHttpClient,urlToken,httpGet);
                //打印信息到console
                System.out.println(user.toString());
               //paper console
               for(article article:papers){
                   System.out.println(article.toString());
               }


                //释放连接
                httpGet.releaseConnection();
                long end=System.currentTimeMillis();
                logger.info(end-start+"ms, data:"+user.getName());

        }catch (Exception e){
            executorQueue.push(this.urlToken);
            logger.error(e.toString());
            for(int i=0;i<e.getStackTrace().length;i++){
                 logger.error(e.getStackTrace()[i]);
            }
        }

    }

}
