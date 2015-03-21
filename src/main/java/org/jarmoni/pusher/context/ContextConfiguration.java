package org.jarmoni.pusher.context;

import org.jarmoni.pusher.controller.util.RepositoryLinkCreator;
import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.pusher.service.PusherService;
import org.jarmoni.restxe.common.IUrlResolver;
import org.jarmoni.restxe.common.LinkFactory;
import org.jarmoni.restxe.spring.ServletRequestUrlResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration {

	@Value("${app.home}")
	private String appHome;

	@Bean
	public IUrlResolver urlResolver() {
		return new ServletRequestUrlResolver();
	}

	@Bean
	public LinkFactory linkFactory() {
		return new LinkFactory(this.urlResolver());
	}

	@Bean
	public RepositoryLinkCreator repositoryLinkCreator() {
		return new RepositoryLinkCreator(this.linkFactory());
	}

	@Bean(initMethod = "init")
	public IPusherService pusherService() {
		return new PusherService(this.appHome, this.gitExecutor());
	}

	@Bean
	GitExecutor gitExecutor() {
		return new GitExecutor();
	}

}
