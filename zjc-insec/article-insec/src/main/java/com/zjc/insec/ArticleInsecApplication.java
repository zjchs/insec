package com.zjc.insec;

import com.zjc.insec.core.InsecCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ArticleInsecApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext=SpringApplication.run(ArticleInsecApplication.class, args);
		InsecCore insecCore=(InsecCore)configurableApplicationContext.getBean("insecCore");
		insecCore.start();
	}
}
