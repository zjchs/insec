package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.entity.person;
import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.until.InsecQueue;
import com.zjc.insec.insec.until.ParseUntil;
import com.zjc.insec.insec.until.StreamUntil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URI;
import java.util.HashMap;

public class InsecRunnable implements Runnable {


    public CloseableHttpClient closeableHttpClient;

    public String proxyUrl;

    public int proxyPort;

    public InsecQueue executorQueue;

    public HashMap<String,String> history;

    public String aimUrl;

    Logger logger= LogManager.getLogger(InsecRunnable.class);

    public InsecRunnable(CloseableHttpClient closeableHttpClient, String proxyUrl, int proxyPort, InsecQueue executorQueue, HashMap<String,String > history,String aimUrl){
        this.closeableHttpClient=closeableHttpClient;
        this.proxyUrl=proxyUrl;
        this.proxyPort=proxyPort;
        this.executorQueue=executorQueue;
        this.history=history;
        this.aimUrl=aimUrl;
    }

    @Override
    public void run(){
        HttpGet httpGet=new HttpGet(this.aimUrl);
        HttpClientUntil.config(httpGet,this.proxyUrl,this.proxyPort);
        try{
            long start=System.currentTimeMillis();
            CloseableHttpResponse closeableHttpResponse=closeableHttpClient.execute(httpGet);
            if(closeableHttpResponse.getStatusLine().getStatusCode()==200) {
                //获取个人信息主体
                HttpEntity httpEntity = closeableHttpResponse.getEntity();
                String content = StreamUntil.steamToStr(httpEntity.getContent());
                person person = ParseUntil.parseByZH(content);
                closeableHttpResponse.close();


                //获取Paper
                httpGet.setURI(new URI(person.getUrl()));
                CloseableHttpResponse closeableHttpResponse1 = closeableHttpClient.execute(httpGet);
                HttpEntity httpEntity1 = closeableHttpResponse1.getEntity();
                String content1 = StreamUntil.steamToStr(httpEntity1.getContent());
                person.setPapers(ParseUntil.parsePapers(content1));
                closeableHttpResponse1.close();


                //获取关注者
                httpGet.setURI(new URI(person.getFocusUrl()));
                CloseableHttpResponse closeableHttpResponse2 = closeableHttpClient.execute(httpGet);
                HttpEntity httpEntity2 = closeableHttpResponse2.getEntity();
                String content2 = StreamUntil.steamToStr(httpEntity2.getContent());
                person.setFocusUrls(ParseUntil.parseFocusUrls(content2));
                closeableHttpResponse2.close();

                //打印信息到console
                for(String focusUrl:person.getFocusUrls()){
                    executorQueue.push(focusUrl);
                }
                System.out.println("name:" + person.getName() + " label:" + person.getLabel() + " answer:" + person.getAnswer() + " question:" + person.getQuestion() + " url:" + person.getUrl()+" date:"+person.getDate()+" fanNums:"+person.getFanNums());
                for(com.zjc.insec.insec.entity.paper paper:person.getPapers()){
                    System.out.println("title:"+paper.getTitile()+" url:"+paper.getUrl()+" num:"+paper.getNum());
                }

                //释放连接
                httpGet.releaseConnection();
                long end=System.currentTimeMillis();
                logger.info(end-start+"ms, data:"+person.getName());
            }
        }catch (Exception e){
            executorQueue.push(this.aimUrl);
            logger.error(e.toString());
            for(int i=0;i<e.getStackTrace().length;i++){
                 logger.error(e.getStackTrace()[i]);
            }
        }

    }

}
