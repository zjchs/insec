package com.zjc.insec.insec.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class person implements Serializable{
    /*
      名称
     */
    private String name;
    /*
    标签
     */
    private String label;
    /*
    回答
     */
    private String answer;
    /*
    提问
     */
    private String question;
    /*
    文章
     */
    private List<paper> papers;

    /*
    文章的url
     */
    private String url;
    /*
    获得该用户的日期
     */
    private Date date;
    /*
    获得赞同
     */
    private String approve;

    /*
    关注者们url
     */
    private List<String> focusUrls;

    /*
    粉丝的数量
     */
    private String  fanNums;

    /*
    关注列表url
     */
    private String focusUrl;

    /*
    setter/getter
     */

    public void setFanNums(String fanNums) {
        this.fanNums = fanNums;
    }

    public String getFanNums() {
        return fanNums;
    }

    public void setFocusUrl(String focusUrl) {
        this.focusUrl = focusUrl;
    }

    public String getFocusUrl() {
        return focusUrl;
    }

    public void setFocusUrls(List<String> focusUrls) {
        this.focusUrls = focusUrls;
    }

    public List<String> getFocusUrls() {
        return focusUrls;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setPapers(List<paper> papers) {
        this.papers = papers;
    }

    public List<paper> getPapers() {
        return papers;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getApprove() {
        return approve;
    }
}
