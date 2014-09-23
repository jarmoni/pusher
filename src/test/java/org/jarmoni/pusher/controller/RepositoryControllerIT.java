package org.jarmoni.pusher.controller;

import java.net.URI;

import org.easymock.EasyMock;
import org.jarmoni.pusher.TstMain;
import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.pusher.service.PusherServiceTest;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.Representation;
import org.jarmoni.restxe.spring.RestTemplateFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

public class RepositoryControllerIT {
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

	/*
	 * public Representation<Repository> getRepository(@PathVariable final
	 * String name) { return Representation.<Repository> builder()
	 * .item(Item.<Repository>
	 * builder().data(this.pusherService.getRepository(name)).build())
	 * .links(this.repositoryLinkCreator.createLinks(name)).build(); }
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetRepository() throws Exception {
		EasyMock.expect(this.pusherService.getRepository("myrepos")).andReturn(PusherServiceTest.createRepository());
		EasyMock.replay(this.pusherService);
		final Representation<Repository> repos = this.restTemplate.getForEntity(
				new URI("http://localhost:9899/api/repository/get/myrepos"), Representation.class).getBody();
		EasyMock.verify(this.pusherService);
	}

}
