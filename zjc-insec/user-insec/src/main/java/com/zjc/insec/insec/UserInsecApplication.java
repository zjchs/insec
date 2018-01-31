package com.zjc.insec.insec;

import com.zjc.insec.insec.core.InsecCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.zjc.common","com.zjc.insec.insec"})
public class UserInsecApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext=SpringApplication.run(UserInsecApplication.class, args);
		InsecCore insecCore=(InsecCore)configurableApplicationContext.getBean("insecCore");
		insecCore.start1();
	}
}
