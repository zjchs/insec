package com.zjc.insec.insec.executorCoreThread;

import com.zjc.common.redis.RedisUntil;
import com.zjc.common.until.StreamUntil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * Created by zjc on 2018/1/21.
 */
public class HistoryRunnable extends Thread {
    Set<String> history;
    public static Logger logger= LogManager.getLogger(HistoryRunnable.class);
    public HistoryRunnable(Set<String> history){
        this.history=history;
    }
    public void run(){
        try {
            long start = System.currentTimeMillis();
            RedisUntil.set(StreamUntil.serializa("history"), StreamUntil.serializeByString(history));
            long end = System.currentTimeMillis();
            logger.info("history url_token save" + (end - start));
        }catch (Exception e){
            logger.error(e.toString());
        }
    }
}
