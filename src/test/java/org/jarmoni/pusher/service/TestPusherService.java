package org.jarmoni.pusher.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jarmoni.pusher.resource.RepositoryResource;

import com.google.common.collect.Lists;

public class TestPusherService implements IPusherService {

	private List<RepositoryResource> repositories = Lists.newArrayList();

	public TestPusherService() {
		this.repositories.add(RepositoryResource.builder().name("repos1").path("/my/path").autoCommit(true).autoSync(false)
				.build());
		this.repositories.add(RepositoryResource.builder().name("repos2").path("/another/path").autoCommit(false).autoSync(false)
				.build());
		this.repositories.add(RepositoryResource.builder().name("repos3").path("/and_another/one").autoCommit(true)
				.autoSync(true).build());
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public List<RepositoryResource> getRepositories() {
		return this.repositories;
	}

	@Override
	public RepositoryResource getRepository(final String name) {
		final Optional<RepositoryResource> result = this.repositories.stream().filter(r -> r.getName().equals(name)).findFirst();
		return result.isPresent() ? result.get() : null;
	}

	@Override
	public RepositoryResource createRepository(final RepositoryResource repository) {
		if (this.repositories.contains(repository)) {
			throw new RuntimeException("Repository already exists=" + repository);
		}
		this.repositories.add(repository);
		return repository;
	}

	@Override
	public void deleteRepository(final String name) {
		this.repositories = this.repositories.stream().filter(r -> !r.getName().equals(name)).collect(Collectors.toList());

	}

	@Override
	public RepositoryResource updateRepository(final RepositoryResource repository) {
		this.deleteRepository(repository.getName());
		this.repositories.add(repository);
		return repository;
	}

	@Override
	public void triggerRepository(final String name) {
		throw new UnsupportedOperationException("Not yet implemented");

	}

}
