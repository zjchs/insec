package com.zjc.insec.insec.entity;

import java.util.List;

/**
 * Created by zjc on 2018/1/7.
 */
public class user {
    /*
    用户标识号
     */
    private String url_token;
    /*
    名称
     */
    private String name;
    /*
    标题
     */
    private String headline;
    /*
    行业
     */
    private String industry_category;
    /*
    简介
     */
    private String description;
    /*
    回答
     */
    private int answer_count;
    /*
    提问
     */
    private int question_count;
    /*
    文章
     */
    private int articles_count;
    /*
    关注者
     */
    private int following_count;
    /*
    粉丝
     */
    private int  follower_count;
    /*
    获得的赞同
     */
    private int voteup_count;
    /*
    获得的感谢
     */
    private int thanked_count;
    /*
    获得的收藏
     */
    private int favorite_count;

    /*
    关注的话题
     */
    private int following_topic_count;

    /*
    关注的话题列表
     */
    private List<String> topics;

    public String getUrl_token() {
        return url_token;
    }

    public void setUrl_token(String url_token) {
        this.url_token = url_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getIndustry_category() {
        return industry_category;
    }

    public void setIndustry_category(String industry_category) {
        this.industry_category = industry_category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public int getQuestion_count() {
        return question_count;
    }

    public void setQuestion_count(int question_count) {
        this.question_count = question_count;
    }

    public int getArticles_count() {
        return articles_count;
    }

    public void setArticles_count(int articles_count) {
        this.articles_count = articles_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getVoteup_count() {
        return voteup_count;
    }

    public void setVoteup_count(int voteup_count) {
        this.voteup_count = voteup_count;
    }

    public int getThanked_count() {
        return thanked_count;
    }

    public void setThanked_count(int thanked_count) {
        this.thanked_count = thanked_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getFollowing_topic_count() {
        return following_topic_count;
    }

    public void setFollowing_topic_count(int following_topic_count) {
        this.following_topic_count = following_topic_count;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    @Override
    public String toString() {
        return "user{" +
                "url_token='" + url_token + '\'' +
                ", name='" + name + '\'' +
                ", headline='" + headline + '\'' +
                ", industry_category='" + industry_category + '\'' +
                ", description='" + description + '\'' +
                ", answer_count=" + answer_count +
                ", question_count=" + question_count +
                ", articles_count=" + articles_count +
                ", following_count=" + following_count +
                ", follower_count=" + follower_count +
                ", voteup_count=" + voteup_count +
                ", thanked_count=" + thanked_count +
                ", favorite_count=" + favorite_count +
                ", following_topic_count=" + following_topic_count +
                '}';
    }
}
