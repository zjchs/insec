package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.http.HttpProxy;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by zjc on 2018/1/18.
 */
public class BaseThread extends Thread {
    public CloseableHttpClient closeableHttpClient;

    public HttpProxy httpProxy;

    public HttpGet httpget;

    public long endTime=System.currentTimeMillis();

    public BaseThread(CloseableHttpClient closeableHttpClient, HttpProxy httpProxy){
        this.closeableHttpClient=closeableHttpClient;
        this.httpProxy=httpProxy;
    }
    public void run(){

    }
}
