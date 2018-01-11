package com.zjc.insec.insec;

import com.zjc.insec.insec.core.InsecCore;
import com.zjc.insec.insec.until.StreamUntil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
public class InsecApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext=SpringApplication.run(InsecApplication.class, args);
		InsecCore insecCore=(InsecCore)configurableApplicationContext.getBean("insecCore");
		insecCore.start1();
	}
}
