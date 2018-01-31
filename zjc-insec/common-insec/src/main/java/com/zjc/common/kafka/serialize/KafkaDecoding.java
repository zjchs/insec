package com.zjc.common.kafka.serialize;


import com.zjc.common.until.StreamUntil;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by zjc on 2018/1/29.
 */
public class KafkaDecoding implements Deserializer {
    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Object deserialize(String s, byte[] bytes) {
        return StreamUntil.deserializeByByte(bytes);
    }

    @Override
    public void close() {

    }


}
