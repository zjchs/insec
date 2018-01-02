package com.zjc.insec.insec.until;

import com.zjc.insec.insec.entity.paper;
import com.zjc.insec.insec.entity.person;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zjc on 2017/12/29.
 */
public class ParseUntil {


    public static person parseByZH(String content){
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
        return  person;
    }

    public static List<paper> parsePapers(String content){
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
        return papers;
    }

}
