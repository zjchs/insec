package com.zjc.insec.insec.entity;

import java.io.Serializable;

/**
 * Created by zjc on 2018/1/7.
 */
public class Article implements Serializable{
    /*
    用户编号
     */
    private String url_token;
    /*
    文章标题
     */
    private String title;
    /*
    文章获得的赞
     */
    private int voteup_count;
    /*
    文章获得的评论
     */
    private int comment_count;

    public String getUrl_token() {
        return url_token;
    }

    public void setUrl_token(String url_token) {
        this.url_token = url_token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "article{" +
                "url_token='" + url_token + '\'' +
                ", title='" + title + '\'' +
                ", voteup_count=" + voteup_count +
                ", comment_count=" + comment_count +
                '}';
    }

    public int getVoteup_count() {
        return voteup_count;
    }

    public void setVoteup_count(int voteup_count) {
        this.voteup_count = voteup_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }
}
