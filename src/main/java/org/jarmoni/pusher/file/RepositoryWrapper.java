package org.jarmoni.pusher.file;

import java.nio.file.WatchKey;

import org.eclipse.jgit.lib.Repository;
import org.jarmoni.pusher.resource.RepositoryResource;

public class RepositoryWrapper {

	// Map<String, Pair<Repository, RepositoryResource>
	private final String name;
	private final Repository repository;
	private final RepositoryResource repositoryResource;
	private final WatchKey watchKey;

	public RepositoryWrapper(final String name, final Repository repository, final RepositoryResource repositoryResource,
			final WatchKey watchKey) {
		super();
		this.name = name;
		this.repository = repository;
		this.repositoryResource = repositoryResource;
		this.watchKey = watchKey;
	}

	public String getName() {
		return name;
	}

	public Repository getRepository() {
		return repository;
	}

	public RepositoryResource getRepositoryResource() {
		return repositoryResource;
	}

	public WatchKey getWatchKey() {
		return watchKey;
	}

}
