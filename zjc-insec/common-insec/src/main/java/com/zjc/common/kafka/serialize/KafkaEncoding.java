package com.zjc.common.kafka.serialize;

import com.zjc.common.until.StreamUntil;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by zjc on 2018/1/29.
 */
public class KafkaEncoding implements Serializer<Object> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        return StreamUntil.serializeByString(o);
    }

    @Override
    public void close() {

    }
}
