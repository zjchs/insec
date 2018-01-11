package com.zjc.insec.insec.until;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zjc on 2017/12/29.
 */
public class InsecQueue {

    private ConcurrentLinkedQueue concurrentLinkedQueue=new ConcurrentLinkedQueue();

    public void push(String aim){
        concurrentLinkedQueue.add(aim);
    }

    public String pop(){
        return (String) concurrentLinkedQueue.poll();
    }
    public int getSize(){
       return concurrentLinkedQueue.size();
    }
    public Object[] toArray(){
        return concurrentLinkedQueue.toArray();
    }

}
