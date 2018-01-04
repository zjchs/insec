package com.zjc.insec.insec.until;

import com.zjc.insec.insec.entity.paper;
import com.zjc.insec.insec.entity.person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zjc on 2017/12/29.
 */
public class ParseUntil {
    public static Logger logger= LogManager.getLogger(ParseUntil.class);

    public static person parseByZH(String content){
        long start =System.currentTimeMillis();
        person person=new person();
        person.setDate(new Date());
        Document document=Jsoup.parse(content);
        Elements elements=document.select("div.ProfileHeader-contentHead");
        for(Element element :elements){
            person.setName(element.select("span.ProfileHeader-name").text());
            person.setLabel(element.select("span.ProfileHeader-headline").text());
        }
        elements=document.select("ul.Tabs.ProfileMain-tabs");
        for(Element element:elements){
            person.setAnswer(element.select("li[aria-controls=Profile-answers]").select("span.Tabs-meta").text());
            person.setQuestion(element.select("li[aria-controls=Profile-asks]").select("span.Tabs-meta").text());
            person.setUrl("https://www.zhihu.com"+element.select("li[aria-controls=Profile-posts]").select("a.Tabs-link").attr("href"));
        }
        elements=document.select("a.Button.NumberBoard-item.Button--plain");
        System.out.println(elements.size());
        Element element=elements.get(0);//bug
        person.setFocusUrl("https://www.zhihu.com"+element.attr("href"));
        element=elements.get(1);
        person.setFanNums(element.select("strong.NumberBoard-itemValue").text());
        long end =System.currentTimeMillis();
        logger.info("parseByZH:"+person.getName()+"   run time:"+(end-start));
        return  person;
    }

    public static List<paper> parsePapers(String content){
        long start =System.currentTimeMillis();
        List<paper> papers=new ArrayList<>();
        Document document=Jsoup.parse(content);
        Elements elements=document.select("div.List-item");
        if(elements.size()!=0) {
            for (Element element : elements) {
                paper paper = new paper();
                paper.setTitile(element.select("h2.ContentItem-title").select("a").text());
                paper.setUrl(element.select("h2.ContentItem-title").select("a").attr("href"));
                if (element.select("div.ArticleItem-extraInfo").select("span.Voters").select("button.Button.Button--plain").size() == 0) {
                    paper.setNum("还没有人为这篇文章点赞哦！亲！点个赞吧");
                } else {
                    paper.setNum(element.select("div.ArticleItem-extraInfo").select("span.Voters").select("button.Button.Button--plain").text());
                }
                papers.add(paper);
            }
        }
        long end =System.currentTimeMillis();
        logger.info("parsePapers run time:"+(end-start));
        return papers;
    }

    public static List<String> parseFocusUrls(String content){
        long start =System.currentTimeMillis();
        List<String> fouckUrls=new ArrayList<>();
        Document document=Jsoup.parse(content);
        Elements elements=document.select("div.UserItem-title");
        if(elements.size()>0){
            for(Element element:elements){
                fouckUrls.add("https://www.zhihu.com"+element.select("a.UserLink-link").attr("href")+"/activities");
            }
        }
        long end =System.currentTimeMillis();
        logger.info("parseFocusUrls run time:"+(end-start));
        return  fouckUrls;
    }

}
