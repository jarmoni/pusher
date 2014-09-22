package org.jarmoni.pusher.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.jarmoni.resource.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class PusherService implements IPusherService {

	private Path appHome;

	private List<Repository> repositories = Lists.newArrayList();

	public PusherService(final String appHome) {
		final Path path = Paths.get(Preconditions.checkNotNull(appHome));
		if (Files.isDirectory(path)) {
			this.appHome = path;
		}
		else {
			try {
				this.appHome = Files.createDirectory(path);
			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	void reloadRepositories() {
		final Path reposFile = this.appHome.resolve("repos.json");
		if (Files.isRegularFile(reposFile)) {
			try (final InputStream is = Files.newInputStream(reposFile)) {
				final ObjectMapper mapper = new ObjectMapper();
				this.repositories = mapper.readValue(is, List.class);

			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	public String getAppHome() {
		return this.appHome.toString();
	}

	@Override
	public List<Repository> getRepositories() {
		return this.repositories;
	}

	@Override
	public Repository getRepository(final String name) {
		return this.repositories.stream().filter(rep -> name.equals(rep.name)).findFirst().get();
	}

	@Override
	public Repository createRepository(final Repository repository) {
		this.repositories.add(repository);
		return repository;
	}

	@Override
	public void deleteRepository(final String name) {
		this.repositories.stream().filter(rep -> !rep.name.equals(name)).collect(Collectors.toList());
	}

	@Override
	public Repository updateRepository(final Repository repository) {
		this.repositories.stream().filter(rep -> !rep.name.equals(repository.name)).collect(Collectors.toList()).add(repository);
		return repository;
	}
}
