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
import redis.clients.jedis.Jedis;

import javax.net.ssl.SSLHandshakeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zjc on 2017/12/29.
 */
@Component
public class InsecCore {
    @Autowired
    public InsecQueue insecQueue;

    @Autowired
    public UrlProxy urlProxy;

    @Autowired
    public CloseableHttpClient closeableHttpClient;


    public void start(){
        Executor executor= Executors.newFixedThreadPool(10);
            int i=10;
            List<String> url=urlProxy.getUrls();
            List<Integer> port=urlProxy.getPorts();
//                for(;i>0;i--){
//                    executor.execute(new InsecRunnable(closeableHttpClient,url.get(i),port.get(i)));
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
        System.out.println(url.get(10)+":"+port.get(10));
    }
}
