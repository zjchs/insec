package com.zjc.article;

import com.zjc.article.core.InsecCore1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.zjc.common","com.zjc.article"})
public class ArticleInsecApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext=SpringApplication.run(ArticleInsecApplication.class, args);
		InsecCore1 insecCore=(InsecCore1)configurableApplicationContext.getBean("insecCore1");
		insecCore.start();
	}
}
