package com.zjc.insec.http;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjc on 2017/12/28.
 */
@Configuration
public class HttpClientUntil {

    @Bean
    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager cm=new PoolingHttpClientConnectionManager(registryHttpOrHttps());
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(150);
        return cm;
    }
    @Bean
    public Registry<ConnectionSocketFactory> registryHttpOrHttps(){
        RegistryBuilder<ConnectionSocketFactory> registryBuilder= RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory connectionSocketFactory=new PlainConnectionSocketFactory();
        registryBuilder.register("http",connectionSocketFactory);
        try{
            KeyStore keyStore=KeyStore.getInstance(KeyStore.getDefaultType());
            SSLContext sslContext= SSLContexts.custom().loadTrustMaterial(keyStore,new AnyTrustStrategy()).build();
            LayeredConnectionSocketFactory layeredConnectionSocketFactory=new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https",layeredConnectionSocketFactory);
        }catch(Exception e){
             System.out.println(e.getMessage());
        }
        return registryBuilder.build();
    }

    @Bean
    public CloseableHttpClient getCloseableHttpClient(){
        HttpRequestRetryHandler httpRequestRetryHandler=new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                if(i>=2){
                    return  false;
                }
                if(e instanceof NoHttpResponseException){
                    return false;
                }
                if(e instanceof SSLHandshakeException){
                    return false;
                }
                if(e instanceof InterruptedIOException){
                    return false;
                }
                if(e instanceof UnknownHostException){
                    return false;
                }
                if(e instanceof ConnectException){
                    return false;
                }
                if(e instanceof SocketException){
                    return  false;
                }
                HttpClientContext httpClientContext= HttpClientContext.adapt(httpContext);
                HttpRequest httpRequest=httpClientContext.getRequest();
                if(!(httpRequest instanceof HttpEntityEnclosingRequest)){
                    return  true;
                }
                return false;
            }
        };
        CloseableHttpClient closeableHttpClient= HttpClients.custom()
                .setConnectionManager(getPoolingHttpClientConnectionManager())
                .setRetryHandler(httpRequestRetryHandler)
                .build();
        return  closeableHttpClient;
    }


    public static void config(HttpRequestBase httpRequestBase,String url,int port){
        httpRequestBase.setHeader("User-Agent","Mozilla/5.0");
        httpRequestBase.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpRequestBase.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//"en-US,en;q=0.5");  
        httpRequestBase.setHeader("Accept-Charset","ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");
        httpRequestBase.setHeader("accept","application/json");
        httpRequestBase.setHeader("authorization","oauth c3cef7c66a1843f8b3a9e6a1e3160e20");

        RequestConfig requestConfig= RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(2000)
                .setProxy(new HttpHost(url,port))
                .build();
        httpRequestBase.setConfig(requestConfig);
    }

}
