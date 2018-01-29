package com.zjc.insec.until;

import java.util.Collection;
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
    public void pushAll(Collection collection){
        concurrentLinkedQueue.addAll(collection);
    }

}
