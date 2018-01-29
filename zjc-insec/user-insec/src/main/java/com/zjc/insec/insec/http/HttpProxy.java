package com.zjc.insec.insec.http;

import com.zjc.insec.insec.until.StreamUntil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zjc on 2018/1/16.
 */
@Component
public class HttpProxy {
    @Autowired
    CloseableHttpClient closeableHttpClient;

    public static Logger logger= LogManager.getLogger(HttpProxy.class);

    public Queue<String> urlQueue=null;

    public Queue<String> urlQueue1=null;

    public Queue<String> urlQueue2=null;


    public void initProxyUrl(Queue<String> queue){
        CloseableHttpResponse closeableHttpResponse=null;
        HttpGet httpGet=null;
        try {
            Long start=System.currentTimeMillis();

            httpGet = new HttpGet("http://tvp.daxiangdaili.com/ip/?tid=559801516032045&num=40&protocol=https&format=json&delay=3");
            closeableHttpResponse=closeableHttpClient.execute(httpGet);
            if(closeableHttpResponse.getStatusLine().getStatusCode()==200){
                HttpEntity  httpEntity=closeableHttpResponse.getEntity();
                if(httpEntity!=null){
                    String content= StreamUntil.steamToStr(httpEntity.getContent());
                    JSONArray jsonArray= JSONArray.fromObject(content);
                    String str="";
                    for(Object jsonObject:jsonArray) {
                        JSONObject jsonObject1 = (JSONObject) jsonObject;
                        str = jsonObject1.getString("host") + ":";
                        str += jsonObject1.getString("port");
                        queue.add(str);
                    }
                    long end =System.currentTimeMillis();
                    logger.info("initUrlProxy:"+(end-start)+"ms");
                }
            }
        }catch (Exception e){
             logger.error("initUrlProxy init error");
        }finally {
            if(closeableHttpResponse !=null){
                try{
                    closeableHttpResponse.close();
                }catch (Exception e){
                    logger.error(e.toString());
                }
            }
            httpGet.abort();
        }
    }
    public synchronized String getProxyUrl(){
        if(urlQueue==null){
            urlQueue=new ConcurrentLinkedQueue<>();
        }
       String proxy=this.urlQueue.poll();
       if(proxy!=null){
           return proxy;
       }else{
           initProxyUrl(urlQueue);
           return this.urlQueue.poll();
       }
    }

    public synchronized String getProxyUrl1(){
        if(urlQueue1==null){
            urlQueue1=new ConcurrentLinkedQueue<>();
        }
        String proxy=this.urlQueue1.poll();
        if(proxy!=null){
            return proxy;
        }else{
            initProxyUrl(urlQueue1);
            return this.urlQueue1.poll();
        }
    }

    public synchronized String getProxyUrl2(){
        if(urlQueue2==null){
            urlQueue2=new ConcurrentLinkedQueue<>();
        }
        String proxy=this.urlQueue2.poll();
        if(proxy!=null){
            return proxy;
        }else{
            initProxyUrl(urlQueue2);
            return this.urlQueue2.poll();
        }
    }
}
