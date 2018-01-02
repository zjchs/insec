package com.zjc.insec.insec.http;

import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Created by zjc on 2018/1/2.
 */

public class UrlProxy {
     private List<String> urls;
     private List<Integer> ports;
    public UrlProxy(){

    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public List<Integer> getPorts() {
        return ports;
    }
}
