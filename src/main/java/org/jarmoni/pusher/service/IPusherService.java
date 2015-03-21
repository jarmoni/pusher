package org.jarmoni.pusher.service;

import java.util.List;

import org.jarmoni.pusher.resource.RepositoryResource;

public interface IPusherService {

	List<RepositoryResource> getRepositories();

	RepositoryResource getRepository(final String name);

	RepositoryResource createRepository(RepositoryResource repository);

	void deleteRepository(final String name);

	RepositoryResource updateRepository(final RepositoryResource repository);

	void triggerRepository(String name);
}
