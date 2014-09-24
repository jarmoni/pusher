package org.jarmoni.pusher.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jarmoni.pusher.service.PusherServiceTest.createRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.jarmoni.pusher.TstMain;
import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.Representation;
import org.jarmoni.restxe.spring.RestTemplateFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class RepositoryControllerIT {
	private final RestTemplate restTemplate = RestTemplateFactory
			.createTemplate(Repository.class);
	private ConfigurableApplicationContext context;
	private IPusherService pusherService;

	@Before
	public void setUp() throws Exception {
		this.context = SpringApplication.run(TstMain.class, new String[0]);
		this.pusherService = (IPusherService) this.context
				.getBean("pusherService");
	}

	@After
	public void tearDown() throws Exception {
		SpringApplication.exit(this.context, () -> 0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetRepository() throws Exception {
		expect(this.pusherService.getRepository("myrepos")).andReturn(
				createRepository());
		replay(this.pusherService);
		final Representation<Repository> response = this.restTemplate
				.getForEntity(
						new URI(
								"http://localhost:9899/api/repository/get/myrepos"),
								Representation.class).getBody();
		verify(this.pusherService);
		assertEquals(1, response.getLinks().size());
		assertEquals(1, response.getItems().size());
		assertNotNull(response.getItems().get(0).getData());
		assertEquals(3, response.getItems().get(0).getLinks().size());
	}

	@Test
	public void testDeleteRepository() throws Exception {
		this.pusherService.deleteRepository("myrepos");
		expectLastCall();
		replay(this.pusherService);
		this.restTemplate.delete(new URI(
				"http://localhost:9899/api/repository/delete/myrepos"));
		verify(this.pusherService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateRepository() throws Exception {
		final Repository repos = createRepository();
		final Repository reposReturn = createRepository();
		reposReturn.autoCommit = !repos.autoCommit;

		expect(this.pusherService.updateRepository(repos)).andReturn(
				reposReturn);
		replay(this.pusherService);
		final Representation<Repository> response = this.restTemplate.exchange(
				new URI("http://localhost:9899/api/repository/update"),
				HttpMethod.PUT, new HttpEntity<Repository>(repos), Representation.class)
				.getBody();
		verify(this.pusherService);
		assertEquals(1, response.getLinks().size());
		assertEquals(1, response.getItems().size());
		assertEquals(response.getItems().get(0).getData(), reposReturn);
		assertEquals(3, response.getItems().get(0).getLinks().size());
	}

}
