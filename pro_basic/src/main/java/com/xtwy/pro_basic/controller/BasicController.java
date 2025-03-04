package com.xtwy.pro_basic.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.xtwy.pro_basic.service.BasicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Configuration
@ComponentScan("com.xtwy")
public class BasicController {


	public static void main(String[] args)
	{
		ApplicationContext context = 
				new AnnotationConfigApplicationContext(BasicController.class);
		        //System.out.println("SpringServer is ok");
		        BasicService basicService = context.getBean(BasicService.class);
		        basicService.testSaveUser();

	}
}
