package com.hexaphor.liveclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class LiveClassAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveClassAppApplication.class, args);
	}

}
