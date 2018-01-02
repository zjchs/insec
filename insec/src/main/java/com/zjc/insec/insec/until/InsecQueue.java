package com.zjc.insec.insec.until;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zjc on 2017/12/29.
 */
@Component
public class InsecQueue {

    private ConcurrentLinkedQueue concurrentLinkedQueue=new ConcurrentLinkedQueue();

    public void push(String aim){
        concurrentLinkedQueue.add(aim);
    }

    public String pop(){
        return (String) concurrentLinkedQueue.poll();
    }

}
