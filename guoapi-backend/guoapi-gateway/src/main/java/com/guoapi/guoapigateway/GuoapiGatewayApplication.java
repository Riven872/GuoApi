package com.guoapi.guoapigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class GuoapiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuoapiGatewayApplication.class, args);
		log.info("8090网关启动成功");
	}

}
