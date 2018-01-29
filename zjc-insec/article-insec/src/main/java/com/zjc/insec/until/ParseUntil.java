package com.zjc.insec.until;

import com.google.gson.Gson;
import com.zjc.insec.entity.Article;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjc on 2017/12/29.
 */
public class ParseUntil {
    public static Logger logger= LogManager.getLogger(ParseUntil.class);

    public static List<Article> parsePapers(CloseableHttpClient closeableHttpClient, String utlToken, HttpGet httpGet)throws Exception{
        long start=System.currentTimeMillis();
        List<Article> papers=new ArrayList<>();
        int pre=0;
        String next1="a";
        String next="https://www.zhihu.com/api/v4/members/"+utlToken+"/articles?include=data%5B" +
                "*%5D.comment_count%2Csuggest_edit%2Cis_normal%2Ccan_comment%2Ccomment_permission%2Cadmin_clos" +
                "ed_comment%2Ccontent%2Cvoteup_count%2Ccreated%2Cupdated%2Cupvoted_followees%2Cvotin" +
                "g%2Creview_info%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&o" +
                "ffset="+pre+"&limit="+(pre+20)+"&sort_by=created";

            httpGet.setURI(new URI(next));
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String data = StreamUntil.steamToStr(httpEntity.getContent());
            closeableHttpResponse.close();
            JSONObject jsonObject = JSONObject.fromObject(data);
            JSONArray jsonObject1 = (JSONArray) jsonObject.get("data");

            for (Object jsonObject2 : jsonObject1) {
                Gson gson=new Gson();
                Article a=(Article)gson.fromJson(((JSONObject)jsonObject2).toString(),Article.class);
                a.setUrl_token(utlToken);
                papers.add(a);
            }

        long end=System.currentTimeMillis();
        logger.info(" get papers"+(end-start));
        httpGet.releaseConnection();
        return papers;
    }

    public static List<String> parseTopics(CloseableHttpClient closeableHttpClient,String urlToken,HttpGet httpGet) throws Exception{
        long start=System.currentTimeMillis();
        List<String> topics=new ArrayList<>();
        int pre=0;
        String next1="a";
        String next="https://www.zhihu.com/api/v4/members/" +urlToken+"/"+
                "following-topic-contributions?include=data%5B*%5D.topic.introduction&offset="+pre+"&limit="+(pre+20);

        while(next1!=null) {
            httpGet.setURI(new URI(next));
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String data = StreamUntil.steamToStr(httpEntity.getContent());
            closeableHttpResponse.close();
            JSONObject jsonObject = JSONObject.fromObject(data);
            JSONArray jsonObject1 = (JSONArray) jsonObject.get("data");
            if(jsonObject1.size()==0){
                break;
            }
            for (Object jsonObject2 : jsonObject1) {
                JSONObject jsonObject3=(JSONObject) ((JSONObject) jsonObject2).get("topic");
                topics.add(jsonObject3.get("name").toString());
            }
            JSONObject jsonObject3 = (JSONObject) jsonObject.get("paging");
            next1 = jsonObject3.get("next").toString();
            pre+=20;
            next="https://www.zhihu.com/api/v4/members/" +urlToken+"/"+
                    "following-topic-contributions?include=data%5B*%5D.topic.introduction&offset="+pre+"&limit="+(pre+20);
            //Thread.sleep(1000);
        }
        long end=System.currentTimeMillis();
        logger.info("get topics"+(end-start));
        httpGet.releaseConnection();
        return topics;
    }
}
