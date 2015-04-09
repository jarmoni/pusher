package org.jarmoni.pusher;

import org.easymock.EasyMock;
import org.jarmoni.pusher.service.IPusherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class TstMain {

	@Bean
	public IPusherService pusherService() {
		final IPusherService mock = EasyMock.createMock(IPusherService.class);
		return mock;
	}

	public static void main(final String[] args) {
		SpringApplication.run(TstMain.class, args);
	}
}
