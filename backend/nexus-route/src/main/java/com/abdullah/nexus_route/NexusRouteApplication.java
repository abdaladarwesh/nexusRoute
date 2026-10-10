package com.abdullah.nexus_route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class NexusRouteApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexusRouteApplication.class, args);
	}

}
