package org.jarmoni.pusher.context;

import org.jarmoni.pusher.service.PusherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration {

	@Value("${app.home}")
	private String appHome;

	@Bean
	public PusherService pusherService() {
		return new PusherService(this.appHome);
	}

}
