package org.jarmoni.pusher.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.jarmoni.resource.Repository;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class PusherService implements IPusherService {

	public static final String REPOS_FILE_NAME = "repos.json";

	private Path appHome;
	private final Path reposFile;

	private List<Repository> repositories = Lists.newArrayList();

	public PusherService(final String appHome) {
		final Path path = Paths.get(Preconditions.checkNotNull(appHome));
		if (Files.isDirectory(path)) {
			this.appHome = path;
		} else {
			try {
				this.appHome = Files.createDirectory(path);
			} catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
		this.reposFile = this.appHome.resolve(REPOS_FILE_NAME);
	}

	void reloadRepositories() {
		if (Files.isRegularFile(this.reposFile)) {
			try (final InputStream is = Files.newInputStream(this.reposFile)) {
				final ObjectMapper mapper = new ObjectMapper();
				this.repositories = mapper.readValue(is, List.class);
			} catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	void saveRepositories() {
		try (OutputStream os = Files.newOutputStream(this.reposFile)) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(os, this.repositories);
		} catch (final Throwable t) {
			Throwables.propagate(t);
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
		return this.repositories.stream().filter(rep -> name.equals(rep.name))
				.findFirst().get();
	}

	@Override
	public Repository createRepository(final Repository repository) {
		this.repositories.add(repository);
		return repository;
	}

	@Override
	public void deleteRepository(final String name) {
		this.repositories.stream().filter(rep -> !rep.name.equals(name))
		.collect(Collectors.toList());
	}

	@Override
	public Repository updateRepository(final Repository repository) {
		this.repositories.stream()
		.filter(rep -> !rep.name.equals(repository.name))
		.collect(Collectors.toList()).add(repository);
		return repository;
	}

	public static void main(final String[] args) {
		final Repository r1 = new Repository();
		r1.autoCommit = true;
		r1.autoPush = false;
		r1.name = "/home/johndoe/myrepos";

		final Repository r2 = new Repository();
		r2.autoCommit = true;
		r2.autoPush = true;
		r2.name = "/usr/local/another_repos";

		final List<Repository> reposList = Lists.newArrayList(r1, r2);
		final ObjectMapper m = new ObjectMapper();
		try {
			m.writeValue(new File("/tmp/aaaaaaaaaaaaaaaaaa.json"), reposList);
		} catch (final JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
