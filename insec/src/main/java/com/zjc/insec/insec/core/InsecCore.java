package com.zjc.insec.insec.core;


import com.zjc.insec.insec.entity.paper;
import com.zjc.insec.insec.entity.person;
import com.zjc.insec.insec.executorCoreThread.InsecRunnable;
import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.http.UrlProxy;
import com.zjc.insec.insec.until.InsecQueue;
import com.zjc.insec.insec.until.ParseUntil;
import com.zjc.insec.insec.until.StreamUntil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by zjc on 2017/12/29.
 */
@Component
public class InsecCore {

    @Autowired
    public UrlProxy urlProxy;

    @Autowired
    public CloseableHttpClient closeableHttpClient;

    public void start(){
        InsecQueue executorQueue=new InsecQueue();
        HashMap<String,String> history=new HashMap<>();
        executorQueue.push("https://www.zhihu.com/people/ivan-39-18/activities");
        Executor executor= Executors.newFixedThreadPool(20);
        int i=0;
        List<String> urls=urlProxy.getUrls();
        List<Integer> port=urlProxy.getPorts();
        while(true){
            String url=executorQueue.pop();
            if(url==null){
                continue;
            }
            if(i>19){
                i=0;
            }
                executor.execute(new InsecRunnable(closeableHttpClient, urls.get(i), port.get(i), executorQueue, history, url));
            i++;
        }
//            int i=10;
//            List<String> url=urlProxy.getUrls();
//            List<Integer> port=urlProxy.getPorts();
//                for(;i>0;i--){
//                    executor.execute(new InsecRunnable(closeableHttpClient,url.get(i),port.get(i),executorQueue,history));
//                    if(i==1){
//                        i=10;
//                        try{
//                            Thread.sleep(1000);
//                        }catch (Exception e){
//                            System.out.println(e.toString());
//                        }
//                        System.out.println("ok");
//                    }
//                }
    }
}
