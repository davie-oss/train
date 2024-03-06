package com.jiawa.train.business.config;


import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;


@ComponentScan("com.jiawa")
@MapperScan("com/jiawa/train/business/mapper")
@SpringBootApplication
public class BusinessApplication {


	private static final Logger log=LoggerFactory.getLogger(BusinessApplication.class);


	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BusinessApplication.class);
		ConfigurableEnvironment env = app.run(args).getEnvironment();
		log.info("启动成功");
		log.info("地址：\thttp;//127.0.0.1:{}",env.getProperty("server.port"));
	}

}
