package org.jarmoni.pusher.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jarmoni.pusher.service.PusherServiceTest.createRepositoryResource;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.List;

import org.easymock.EasyMock;
import org.jarmoni.pusher.resource.RepositoryResource;
import org.junit.Test;

import com.google.common.collect.Lists;

public class RepositoriesControllerIT extends AbstractControllerIT {

	@SuppressWarnings("unchecked")
	@Test
	public void testListRepositories() throws Exception {
		expect(this.getPusherService().getRepositories()).andReturn(Lists.newArrayList(createRepositoryResource()));
		replay(this.getPusherService());
		final List<RepositoryResource> response = this.getRestTemplate()
				.getForEntity(new URI("http://localhost:9899" + RepositoriesController.PATH_REPOSITORIES_LIST), List.class)
				.getBody();
		verify(this.getPusherService());
		assertEquals(1, response.size());
	}

	@Test
	public void testCreateRepository() throws Exception {
		final RepositoryResource repos = createRepositoryResource();
		EasyMock.expect(this.getPusherService().createRepository(repos)).andReturn(createRepositoryResource());
		replay(this.getPusherService());
		final RepositoryResource result = this
				.getRestTemplate()
				.postForEntity(new URI("http://localhost:9899" + RepositoriesController.PATH_REPOSITORIES_CREATE), repos,
						RepositoryResource.class).getBody();
		verify(this.getPusherService());
		assertEquals(repos, result);
	}
}
