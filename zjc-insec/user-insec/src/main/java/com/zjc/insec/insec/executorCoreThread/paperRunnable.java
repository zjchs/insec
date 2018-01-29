package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.entity.Article;
import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.until.ParseUntil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

/**
 * Created by zjc on 2018/1/7.
 */
public class paperRunnable implements Runnable {

    public String urlToken;

    public CloseableHttpClient closeableHttpClient;

    public String proxyUrl;

    public int proxyPort;

    public paperRunnable(String urlToken, CloseableHttpClient closeableHttpClient,String proxyUrl,int proxyPort){
        this.urlToken=urlToken;
        this.closeableHttpClient=closeableHttpClient;
        this.proxyUrl=proxyUrl;
        this.proxyPort=proxyPort;
    }

    @Override
    public void run() {
        HttpGet httpGet=new HttpGet();
        HttpClientUntil.config(httpGet,this.proxyUrl,this.proxyPort);
        try {
           List<Article> papers= ParseUntil.parsePapers(closeableHttpClient, urlToken, httpGet);
            for(Article article:papers){
                System.out.println(article.toString());
            }
            httpGet.releaseConnection();
        }catch(Exception e){

        }
    }
}
