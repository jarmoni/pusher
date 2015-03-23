package org.jarmoni.pusher.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jarmoni.pusher.service.PusherServiceTest.createRepositoryResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.jarmoni.pusher.resource.RepositoryResource;
import org.jarmoni.restxe.common.Representation;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class RepositoryControllerIT extends AbstractControllerIT {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetRepository() throws Exception {
		expect(this.getPusherService().getRepository("myrepos")).andReturn(createRepositoryResource());
		replay(this.getPusherService());
		final Representation<RepositoryResource> response = this
				.getRestTemplate()
				.getForEntity(
						new URI("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_GET.replace("{name}", "myrepos")),
						Representation.class).getBody();
		verify(this.getPusherService());
		assertEquals(1, response.getLinks().size());
		assertEquals(1, response.getItems().size());
		assertNotNull(response.getItems().get(0).getData());
		assertEquals(3, response.getItems().get(0).getLinks().size());
	}

	@SuppressWarnings("unchecked")
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
		final Representation<RepositoryResource> response = this
				.getRestTemplate()
				.exchange(new URI("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_UPDATE), HttpMethod.PUT,
						new HttpEntity<RepositoryResource>(repos), Representation.class).getBody();
		verify(this.getPusherService());
		assertEquals(1, response.getLinks().size());
		assertEquals(1, response.getItems().size());
		assertEquals(response.getItems().get(0).getData(), reposReturn);
		assertFalse(response.getItems().get(0).getData().isAutoCommit());
		assertEquals(3, response.getItems().get(0).getLinks().size());
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

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetRepositoryExThrown() throws Exception {
		expect(this.getPusherService().getRepository("myrepos")).andThrow(
				new RuntimeException("Something very bad has happened..."));
		replay(this.getPusherService());
		final ResponseEntity<Representation> response = this.getRestTemplate().getForEntity(
				new URI("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_GET.replace("{name}", "myrepos")),
				Representation.class);
		verify(this.getPusherService());
		assertFalse(response.getStatusCode().is2xxSuccessful());
		assertEquals("Something very bad has happened...", response.getBody().getErrorMessage());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testTriggerRepositoryExThrown() throws Exception {
		this.getPusherService().triggerRepository("myrepos");
		expectLastCall().andThrow(new RuntimeException("Even worse..."));
		replay(this.getPusherService());
		final URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9899" + RepositoryController.PATH_REPOSITORY_TRIGGER)
				.queryParam("name", "myrepos").build().toUri();
		final ResponseEntity<Representation> response = this.getRestTemplate().postForEntity(uri, "", Representation.class);
		verify(this.getPusherService());
		assertFalse(response.getStatusCode().is2xxSuccessful());
		assertEquals("Even worse...", response.getBody().getErrorMessage());
	}

}
