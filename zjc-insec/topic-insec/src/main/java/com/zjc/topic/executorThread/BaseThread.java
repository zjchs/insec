package com.zjc.topic.executorThread;

import com.zjc.common.http.config.HttpClientUntil;
import com.zjc.common.http.until.HttpProxy;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by zjc on 2018/1/18.
 */
public class BaseThread extends Thread {
    public CloseableHttpClient closeableHttpClient;

    public HttpProxy httpProxy;

    public HttpGet httpGet;

    public long endTime=System.currentTimeMillis();

    public BaseThread(CloseableHttpClient closeableHttpClient, HttpProxy httpProxy){
        this.closeableHttpClient=closeableHttpClient;
        this.httpProxy=httpProxy;
    }
    public void run(){

    }
    public HttpGet initUrlProxy(String urlProxy){
          HttpGet httpGet=new HttpGet();
          String url=urlProxy.split(":")[0];
          int port=Integer.parseInt(urlProxy.split(":")[1]);
          HttpClientUntil.config(httpGet,url,port);
          return httpGet;
    }
}
