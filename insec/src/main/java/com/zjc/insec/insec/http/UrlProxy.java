package com.zjc.insec.insec.http;

import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Created by zjc on 2018/1/2.
 */

public class UrlProxy {
     public static List<String> urls;
     public static List<Integer> ports;
     public static int i=0;
    public UrlProxy(){

    }

    public synchronized static String getProxy(){
        if (i > (urls.size() - 1)) {
            i = 0;
        }
        String proxy=urls.get(i)+":"+ports.get(i);
        i++;
        return proxy;
    }
}
