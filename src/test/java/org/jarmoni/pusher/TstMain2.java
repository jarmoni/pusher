package org.jarmoni.pusher;

import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.pusher.service.TestPusherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class TstMain2 {

	@Bean
	public IPusherService pusherService() {
		return new TestPusherService();
	}

	public static void main(final String[] args) {
		SpringApplication.run(TstMain2.class, args);
	}
}
