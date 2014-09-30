package org.jarmoni.pusher.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jarmoni.resource.RepositoryResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class PusherService implements IPusherService {

	public static final String REPOS_FILE_NAME = "repos.json";

	private Path appHome;
	private final Path reposFile;

	private List<RepositoryResource> repositories = Lists.newArrayList();

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
		this.reposFile = this.appHome.resolve(REPOS_FILE_NAME);
	}

	@SuppressWarnings("unchecked")
	void reloadRepositories() {
		if (Files.isRegularFile(this.reposFile)) {
			try (final InputStream is = Files.newInputStream(this.reposFile)) {
				final ObjectMapper mapper = new ObjectMapper();
				this.repositories = mapper.readValue(is, List.class);
			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	void saveRepositories() {
		try (OutputStream os = Files.newOutputStream(this.reposFile)) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(os, this.repositories);
		}
		catch (final Throwable t) {
			Throwables.propagate(t);
		}
	}

	public String getAppHome() {
		return this.appHome.toString();
	}

	@Override
	public List<RepositoryResource> getRepositories() {
		return this.repositories;
	}

	@Override
	public RepositoryResource getRepository(final String name) {
		return this.repositories.stream().filter(rep -> name.equals(rep.getName()))
				.findFirst().orElseThrow(() -> new RuntimeException("Repository does not exist=" + name));
	}

	@Override
	public RepositoryResource createRepository(final RepositoryResource repository) {
		this.repositories.add(repository);
		return repository;
	}

	@Override
	public void deleteRepository(final String name) {
		this.repositories = this.repositories.stream().filter(rep -> !rep.getName().equals(name))
		.collect(Collectors.toList());
	}

	@Override
	public RepositoryResource updateRepository(final RepositoryResource repository) {
		this.repositories = this.repositories.stream()
		.filter(rep -> !rep.getName().equals(repository.getName()))
		.collect(Collectors.toList());
		this.repositories.add(repository);
		return repository;
	}

	@Override
	public void triggerRepository(final String name) {
		// TODO Do the trigger-stuff here.....
	}
}
