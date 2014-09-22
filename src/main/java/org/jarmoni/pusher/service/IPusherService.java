package org.jarmoni.pusher.service;

import java.util.List;

import org.jarmoni.resource.Repository;

public interface IPusherService {

	List<Repository> getRepositories();

	Repository getRepository(final String name);

	Repository createRepository(Repository repository);

	void deleteRepository(final String name);

	Repository updateRepository(final Repository repository);

	void triggerRepository(String name);
}
