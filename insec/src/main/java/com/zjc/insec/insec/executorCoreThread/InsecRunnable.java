package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.entity.person;
import com.zjc.insec.insec.http.HttpClientUntil;
import com.zjc.insec.insec.until.ParseUntil;
import com.zjc.insec.insec.until.StreamUntil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URI;

public class InsecRunnable implements Runnable {


    public CloseableHttpClient closeableHttpClient;

    Logger logger= LogManager.getLogger(InsecRunnable.class);

    public InsecRunnable(CloseableHttpClient closeableHttpClient){
        this.closeableHttpClient=closeableHttpClient;
    }

    @Override
    public void run() {

        HttpGet httpGet=new HttpGet("https://www.zhihu.com/people/ivan-39-18/activities");
        HttpClientUntil.config(httpGet);
        try{
            long start=System.currentTimeMillis();
            CloseableHttpResponse closeableHttpResponse=closeableHttpClient.execute(httpGet);
            if(closeableHttpResponse.getStatusLine().getStatusCode()==200) {

                HttpEntity httpEntity = closeableHttpResponse.getEntity();
                String content = StreamUntil.steamToStr(httpEntity.getContent());
                person person = ParseUntil.parseByZH(content);
                closeableHttpResponse.close();
                httpGet.setURI(new URI(person.getUrl()));
                CloseableHttpResponse closeableHttpResponse1 = closeableHttpClient.execute(httpGet);
                HttpEntity httpEntity1 = closeableHttpResponse1.getEntity();
                String content1 = StreamUntil.steamToStr(httpEntity1.getContent());
                person.setPapers(ParseUntil.parsePapers(content1));
                System.out.println("name:" + person.getName() + " label:" + person.getLabel() + " answer:" + person.getAnswer() + " question:" + person.getQuestion() + " url:" + person.getUrl()+" date:"+person.getDate());
                for(com.zjc.insec.insec.entity.paper paper:person.getPapers()){
                    System.out.println("title:"+paper.getTitile()+" url:"+paper.getUrl()+" num:"+paper.getNum());
                }
                closeableHttpResponse1.close();
                httpGet.releaseConnection();
                long end=System.currentTimeMillis();
                logger.info(end-start+"ms");
            }
        }catch (Exception e){
            logger.error(e.toString());

            for(int i=0;i<e.getStackTrace().length;i++){
                 logger.error(e.getStackTrace()[i]);
            }
        }

    }
}
