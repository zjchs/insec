package com.zjc.common.until;

import com.google.gson.Gson;

import com.zjc.common.entity.Article;
import com.zjc.common.entity.User;
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

    public static User parseUser(CloseableHttpClient closeableHttpClient, String urlToken, HttpGet httpGet)throws Exception{
        long start=System.currentTimeMillis();
        CloseableHttpResponse closeableHttpResponse=null;
        User u=null;
        String data=null;
        String userUrl="https://www.zhihu.com/api/v4/members/"+urlToken+"?include=locations%" +
                "2Cemployments%2Cgender%2Ceducations%2Cbusiness%2Cvoteup_count%2Cthanked_Count%2" +
                "Cfollower_count%2Cfollowing_count%2Ccover_url%2Cfollowing_topic_count%2Cfollowing_question_" +
                "count%2Cfollowing_favlists_count%2Cfollowing_columns_count%2Cavatar_hue%2Canswer_count%2Ca" +
                "rticles_count%2Cpins_count%2Cquestion_count%2Ccolumns_count%2Ccommercial_question_count%2" +
                "Cfavorite_count%2Cfavorited_count%2Clogs_count%2Cincluded_answers_count%2Cincluded_articles_" +
                "count%2Cincluded_text%2Cmessage_thread_token%2Caccount_status%2Cis_active%2Cis_bind_phone%2" +
                "Cis_force_renamed%2Cis_bind_sina%2Cis_privacy_protected%2Csina_weibo_url%2Csina_weibo_name%" +
                "2Cshow_sina_weibo%2Cis_blocking%2Cis_blocked%2Cis_following%2Cis_followed%2Cis_org_" +
                "createpin_white_user%2Cmutual_followees_count%2Cvote_to_count%2Cvote_from_count%2Cthank_" +
                "to_count%2Cthank_from_count%2Cthanked_count%2Cdescription%2Chosted_live_count%2Cparticipated_live_count%2Callow_message%2" +

                "Cindustry_category%2Corg_name%2Corg_homepage%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics";
        try {
            httpGet.setURI(new URI(userUrl));
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            if(httpEntity!=null) {
                 data = StreamUntil.steamToStr(httpEntity.getContent());
            }
            System.out.println(data);
          //  closeableHttpResponse.close();
        }catch (Exception e){
            if(closeableHttpResponse!=null){
                EntityUtils.consume(closeableHttpResponse.getEntity());
                closeableHttpResponse.close();
            }
            httpGet.abort();
            throw e;
        }finally {
            if(closeableHttpResponse!=null){
                EntityUtils.consume(closeableHttpResponse.getEntity());
                closeableHttpResponse.close();
            }
        }
        Gson gson = new Gson();
        u = gson.fromJson(data, User.class);
        long end=System.currentTimeMillis();
        logger.info("get user:"+(end-start));
        return u;
    }



    public static List<String> parseFollowees(CloseableHttpClient closeableHttpClient,String utlToken,HttpGet httpGet) throws Exception{
        long start=System.currentTimeMillis();
        List<String> folloeees=new ArrayList<>();
        int pre=0;
        CloseableHttpResponse closeableHttpResponse=null;
        String data=null;
        String next1="a";
        String next="https://www.zhihu.com/api/v4/members/"+utlToken+"/followees?include=data%5B*%5D.answer_count%2Carticles_count%" +
                "2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F" +
                "(type%3Dbest_answerer)%5D.topics&offset="+pre+"&limit="+(pre+20);
        try {

            httpGet.setURI(new URI(next));
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            if(httpEntity!=null) {
                data = StreamUntil.steamToStr(httpEntity.getContent());
            }
        }catch (Exception e){
            if(closeableHttpResponse!=null){
                EntityUtils.consume(closeableHttpResponse.getEntity());
                closeableHttpResponse.close();
            }
            httpGet.abort();
            throw e;
        }finally {
            if(closeableHttpResponse!=null){
                EntityUtils.consume(closeableHttpResponse.getEntity());
                closeableHttpResponse.close();
            }
        }
            JSONObject jsonObject = JSONObject.fromObject(data);
            JSONArray jsonObject1 = (JSONArray) jsonObject.get("data");

            for (Object jsonObject2 : jsonObject1) {
                folloeees.add(((JSONObject) jsonObject2).get("url_token").toString());
            }

        long end=System.currentTimeMillis();
        logger.info("get followees"+(end-start));
        return folloeees;
    }
    public static List<Article> parsePapers(CloseableHttpClient closeableHttpClient, String utlToken, HttpGet httpGet)throws Exception{
        long start=System.currentTimeMillis();
        List<Article> papers=new ArrayList<>();
        int pre=0;
        String next1="a";
        CloseableHttpResponse closeableHttpResponse=null;
        String data=null;
        String next="https://www.zhihu.com/api/v4/members/"+utlToken+"/articles?include=data%5B" +
                "*%5D.comment_count%2Csuggest_edit%2Cis_normal%2Ccan_comment%2Ccomment_permission%2Cadmin_clos" +
                "ed_comment%2Ccontent%2Cvoteup_count%2Ccreated%2Cupdated%2Cupvoted_followees%2Cvotin" +
                "g%2Creview_info%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&o" +
                "ffset="+pre+"&limit="+(pre+20)+"&sort_by=created";
        try {
            httpGet.setURI(new URI(next));
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            data = StreamUntil.steamToStr(httpEntity.getContent());
            closeableHttpResponse.close();
        }catch (Exception e){
            if(closeableHttpResponse!=null){
                EntityUtils.consume(closeableHttpResponse.getEntity());
                closeableHttpResponse.close();
            }
            httpGet.abort();
            throw e;
        }finally {
            if(closeableHttpResponse!=null){
                EntityUtils.consume(closeableHttpResponse.getEntity());
                closeableHttpResponse.close();
            }
        }
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
        return papers;
    }

    public static List<String> parseTopics(CloseableHttpClient closeableHttpClient,String urlToken,HttpGet httpGet) throws Exception{
        long start=System.currentTimeMillis();
        List<String> topics=new ArrayList<>();
        int pre=0;
        String next1="a";
        String next="https://www.zhihu.com/api/v4/members/" +urlToken+"/"+
                "following-topic-contributions?include=data%5B*%5D.topic.introduction&offset="+pre+"&limit="+(pre+20);
        CloseableHttpResponse closeableHttpResponse=null;
        String data=null;
        while(next1!=null) {
            try {
                httpGet.setURI(new URI(next));
                closeableHttpResponse = closeableHttpClient.execute(httpGet);
                HttpEntity httpEntity = closeableHttpResponse.getEntity();
                data = StreamUntil.steamToStr(httpEntity.getContent());
                closeableHttpResponse.close();
            }catch (Exception e){
                if(closeableHttpResponse!=null){
                    EntityUtils.consume(closeableHttpResponse.getEntity());
                    closeableHttpResponse.close();
                }
                httpGet.abort();
                throw  e;
            }finally {
                if(closeableHttpResponse!=null){
                    EntityUtils.consume(closeableHttpResponse.getEntity());
                    closeableHttpResponse.close();
                }
            }
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
        return topics;
    }

}
