package org.jarmoni.pusher.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jarmoni.pusher.service.PusherServiceTest.createRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.Representation;
import org.junit.Test;

import com.google.common.collect.Lists;

public class RepositoriesControllerIT extends AbstractControllerIT {

	@SuppressWarnings("unchecked")
	@Test
	public void testListRepositories() throws Exception {
		expect(this.getPusherService().getRepositories()).andReturn(Lists.newArrayList(createRepository()));
		replay(this.getPusherService());
		final Representation<Repository> response = this
				.getRestTemplate()
				.getForEntity(new URI("http://localhost:9899" + RepositoriesController.PATH_REPOSITORIES_LIST),
						Representation.class).getBody();
		verify(this.getPusherService());
		assertEquals(2, response.getLinks().size());
		assertEquals(1, response.getItems().size());
		assertNotNull(response.getItems().get(0).getData());
		assertEquals(3, response.getItems().get(0).getLinks().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateRepository() throws Exception {
		final Repository repos = createRepository();
		expect(this.getPusherService().createRepository(repos)).andReturn(repos);
		replay(this.getPusherService());
		final Representation<Repository> response = this
				.getRestTemplate()
				.postForEntity(new URI("http://localhost:9899" + RepositoriesController.PATH_REPOSITORIES_CREATE), repos,
						Representation.class).getBody();
		verify(this.getPusherService());
		assertEquals(1, response.getLinks().size());
		assertEquals(1, response.getItems().size());
		assertNotNull(response.getItems().get(0).getData());
		assertEquals(3, response.getItems().get(0).getLinks().size());
	}
}
