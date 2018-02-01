package com.zjc.common.until;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zjc on 2017/12/29.
 */
public class InsecQueue {

    private ConcurrentLinkedDeque concurrentLinkedDeque=new ConcurrentLinkedDeque();
    public void push(String aim){
        concurrentLinkedDeque.add(aim);
    }

    public String pop(){
        return (String) concurrentLinkedDeque.poll();
    }
    public int getSize(){
       return concurrentLinkedDeque.size();
    }
    public Object[] toArray(){
        return concurrentLinkedDeque.toArray();
    }
    public void pushAll(Collection collection){
        concurrentLinkedDeque.addAll(collection);
    }
    public void pushFirst(String aim){
        concurrentLinkedDeque.addFirst(aim);
    }
}
