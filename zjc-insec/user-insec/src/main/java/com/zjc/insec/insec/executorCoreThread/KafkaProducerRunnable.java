package com.zjc.insec.insec.executorCoreThread;

import com.zjc.common.kafka.producer.KafkaProducerConfig;
import com.zjc.common.until.InsecQueue;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjc on 2018/1/30.
 */
public class KafkaProducerRunnable extends Thread{

    public Producer<String,Object> kafkaProducer;
    public InsecQueue articleQueue;
    public static Logger logger= LogManager.getLogger(KafkaProducerRunnable.class);
    public KafkaProducerRunnable(InsecQueue articleQueue){
         this.kafkaProducer= KafkaProducerConfig.getKafkaProducer();
         this.articleQueue=articleQueue;
    }

       public void run(){
        int i =0;
            while(true){
                try {
                if(articleQueue.getSize()>50){
                        List<String> list=new ArrayList<>();
                        logger.info("kafka start sending message");
                        for(int b=0;b<50;b++){
                            list.add(articleQueue.pop());
                        }
                        long start=System.currentTimeMillis();
                        ProducerRecord<String, Object> record = new ProducerRecord<String, Object>("article", null, list);
                    ProducerRecord<String, Object> record1 = new ProducerRecord<String, Object>("topic", null, list);
                        kafkaProducer.send(record).get();
                        kafkaProducer.send(record1).get();
                        long end=System.currentTimeMillis();
                        logger.info("kafka send message completed:"+(end-start));
                }
                Thread.sleep(1000);
            }catch (Exception e){
               logger.error("kafka send message failed"+e.toString());
           }
       }
}
}
