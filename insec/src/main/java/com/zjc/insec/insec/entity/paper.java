package com.zjc.insec.insec.entity;

import java.io.Serializable;

public class paper implements Serializable{
        /*
         标题
         */
        private String titile;
        /*
        url
         */
        private String url;
        /*
        点赞
         */
        private String num;


        /*
        settet/getter
         */

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getTitile() {
        return titile;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }
}
