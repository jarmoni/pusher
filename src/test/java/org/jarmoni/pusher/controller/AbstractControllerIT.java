package org.jarmoni.pusher.controller;

import org.jarmoni.pusher.TstMain;
import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.spring.RestTemplateFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractControllerIT {

	private final RestTemplate restTemplate = RestTemplateFactory.createTemplate(Repository.class);
	private ConfigurableApplicationContext context;
	private IPusherService pusherService;

	@Before
	public void setUp() throws Exception {
		this.context = SpringApplication.run(TstMain.class, new String[0]);
		this.pusherService = (IPusherService) this.context.getBean("pusherService");
	}

	@After
	public void tearDown() throws Exception {
		SpringApplication.exit(this.context, () -> 0);
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public IPusherService getPusherService() {
		return pusherService;
	}

}
