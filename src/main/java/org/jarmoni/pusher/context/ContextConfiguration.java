package org.jarmoni.pusher.context;

import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.pusher.service.PusherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration {

	@Value("${app.home}")
	private String appHome;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public IPusherService pusherService() {
		return new PusherService(this.appHome, this.gitExecutor());
	}

	@Bean
	GitExecutor gitExecutor() {
		return new GitExecutor();
	}

}
