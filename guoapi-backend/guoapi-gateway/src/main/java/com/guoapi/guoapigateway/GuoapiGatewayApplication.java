package com.guoapi.guoapigateway;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Slf4j
@EnableDubbo
public class GuoapiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuoapiGatewayApplication.class, args);
		log.info("8090网关启动成功");
	}

}
