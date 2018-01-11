package com.zjc.insec.insec.executorCoreThread;

import com.zjc.insec.insec.until.InsecQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;


/**
 * Created by zjc on 2018/1/11.
 */

public class LoadRunnable implements Runnable {

    public static Logger logger=LogManager.getLogger(LoadRunnable.class);

    public int i=1000;

    public InsecQueue userQueue;

    public LoadRunnable(InsecQueue userQueue){
        this.userQueue=userQueue;
    }

    @Override
    public void run() {
        while(true){
            if(userQueue.getSize()>i){
                long start=System.currentTimeMillis();
                Object[] s=(Object[])userQueue.toArray();
                int b=s.length-100;
                int c=b+100;
                String str="";
                for(;b<c;b++){
                    if(b==(c-1)){
                        str+=s[b];
                        break;
                    }
                    str+=s[b]+",";
                }
                File file=new File("E:\\save.log");
                if(file.exists()){
                    file.delete();
                }
                FileWriter fileWriter=null;
                try {
                    file.createNewFile();
                    fileWriter=new FileWriter(file);
                    fileWriter.write(str);
                    fileWriter.close();
                }catch (Exception e){

                }
                long end=System.currentTimeMillis();
                logger.info("save completed- -"+str+"   "+(end-start));
                //System.out.println("save completed- -"+str);
                str="";
                i=i*2;
            }
            try {
                Thread.sleep(2000);
            }catch (Exception e){

            }
        }
    }
}
