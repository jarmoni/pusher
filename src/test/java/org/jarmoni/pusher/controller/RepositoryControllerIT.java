package org.jarmoni.pusher.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jarmoni.pusher.service.PusherServiceTest.createRepositoryResource;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.jarmoni.pusher.resource.RepositoryResource;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

public class RepositoryControllerIT extends AbstractControllerIT {

	@Test
	public void testGetRepository() throws Exception {
		expect(this.getPusherService().getRepository("myrepos")).andReturn(createRepositoryResource());
		replay(this.getPusherService());
		final RepositoryResource response = this
				.getRestTemplate()
				.getForEntity(
						new URI("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_GET.replace("{name}", "myrepos")),
						RepositoryResource.class).getBody();
		verify(this.getPusherService());
		assertEquals(createRepositoryResource(), response);
	}

	@Test
	public void testUpdateRepository() throws Exception {
		// TODO: Strange beheaviour. Refresh of ApplicationContext seems to fail
		// if no timeout is set. Check this....
		Thread.sleep(5000L);
		final RepositoryResource repos = createRepositoryResource();
		final RepositoryResource reposReturn = RepositoryResource.builder().name(repos.getName()).path(repos.getPath())
				.autoCommit(!repos.isAutoCommit()).build();

		expect(this.getPusherService().updateRepository(repos)).andReturn(reposReturn);
		replay(this.getPusherService());
		final RepositoryResource response = this
				.getRestTemplate()
				.exchange(new URI("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_UPDATE), HttpMethod.PUT,
						new HttpEntity<RepositoryResource>(repos), RepositoryResource.class).getBody();
		verify(this.getPusherService());
		Assert.assertNotSame(repos.isAutoCommit(), response.isAutoCommit());
	}

	@Test
	public void testDeleteRepository() throws Exception {
		this.getPusherService().deleteRepository("myrepos");
		expectLastCall();
		replay(this.getPusherService());
		this.getRestTemplate().delete(
				new URI("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_DELETE.replace("{name}", "myrepos")));
		verify(this.getPusherService());
	}

	@Test
	public void testTriggerRepository() throws Exception {
		this.getPusherService().triggerRepository("myrepos");
		expectLastCall();
		replay(this.getPusherService());
		final URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_TRIGGER)
				.queryParam("name", "myrepos").build().toUri();
		this.getRestTemplate().postForEntity(uri, "", Object.class);
		verify(this.getPusherService());
	}
}
