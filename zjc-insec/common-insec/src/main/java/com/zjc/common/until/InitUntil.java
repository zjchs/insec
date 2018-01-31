package com.zjc.common.until;

import com.zjc.common.redis.RedisUntil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zjc on 2018/1/11.
 */
@Component
public class InitUntil {
    public static Logger logger=LogManager.getLogger(InitUntil.class);

    public static void initQueue(InsecQueue userQueue){
        long start=System.currentTimeMillis();
        File file=new File("e:\\save.log");
        FileReader fileReader=null;
        try {
            if (file.exists()) {
                try {
                    fileReader = new FileReader(file);
                    char[] buf = new char[1024];
                    int num;
                    String str = "";
                    while ((num = fileReader.read(buf)) != -1) {
                        str += new String(buf, 0, num);
                    }
                    //fileReader.close();
                    if (str != null) {
                        String[] inputs = str.split(",");
                        for (String input : inputs) {
                            userQueue.push(input);
                        }
                    } else {
                        userQueue.push("ivan-39-18");
                    }

                } catch (Exception e) {

                }
            } else {
                userQueue.push("ivan-39-18");
            }
            long end = System.currentTimeMillis();
            logger.info("initData:" + (end - start)+"ms");
            fileReader.close();
        }catch (Exception e){

        }
    }
    public static Set<String> loadHistorySet(){
         Set<String> history=(Set<String>)(StreamUntil.deserializeByByte(RedisUntil.get(StreamUntil.serializa("history"))));
         return  history==null?new HashSet<>():history;
    }
}
