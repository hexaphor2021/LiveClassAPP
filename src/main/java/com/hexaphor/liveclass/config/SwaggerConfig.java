package com.hexaphor.liveclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 
public class SwaggerConfig {

	@Bean
	public Docket createDocket() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.hexaphor.liveclass.restcontroller"))
				.paths(PathSelectors.regex("/rest.*"))
				.build()
				.apiInfo(alpInfo());
	}
	
	@SuppressWarnings("deprecation")
	private ApiInfo alpInfo() {
		return new ApiInfo("WELCOME TO HEXAPHOR ONLINE LIVE  APPLICATION ", 
				"This application use for online live class application",
				"version 1.0", 
				"http://www.hexaphor.com/", 
				"7008573915",
				"hexaphor licence", 
				"http://www.hexaphor.com/");
	}
}
