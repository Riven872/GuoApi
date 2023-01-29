package com.guoapi.guoapiinterface;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class GuoapiInterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuoapiInterfaceApplication.class, args);
		log.info("GuoapiInterfaceApplication启动成功");
	}

}
