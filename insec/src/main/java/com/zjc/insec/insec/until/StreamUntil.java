package com.zjc.insec.insec.until;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;

public class StreamUntil {
    private static Logger logger= LogManager.getLogger("StreamUntil");
    public static String steamToStr(InputStream inputStream){
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder content=new StringBuilder();
        String line="";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                bufferedReader.close();
            }catch (IOException e){
                System.out.println(e.getMessage());
                logger.error(e.getStackTrace());
            }
        }
        return content.toString();
    }
    public static byte[] serializeByString(Object o){
        ByteArrayOutputStream byteArrayOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        byte[] bytes=null;
        try{
            byteArrayOutputStream=new ByteArrayOutputStream();
            objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
            bytes=byteArrayOutputStream.toByteArray();
        }catch (IOException e){
            logger.error(e.getStackTrace());
        }
        return bytes;
    }
    public static Object deserializeByByte(byte[] bytes){
        if(bytes==null){
            return null;
        }
        ObjectInputStream objectInputStream=null;
        ByteArrayInputStream byteInputStream=null;
        Object o=null;
        try{
            byteInputStream=new ByteArrayInputStream(bytes);
            objectInputStream=new ObjectInputStream(byteInputStream);
            o=objectInputStream.readObject();
        }catch (Exception e){
            logger.error(e.getStackTrace());
        }
        return o;
    }
    public static byte[] serializa(String content){
        return content==null?null:content.getBytes(Charset.forName("utf-8"));
    }
}
