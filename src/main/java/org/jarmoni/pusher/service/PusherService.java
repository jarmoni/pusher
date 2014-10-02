package org.jarmoni.pusher.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Repository;
import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.resource.RepositoryResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

public class PusherService implements IPusherService {

	public static final String REPOS_FILE_NAME = "repos.json";

	private Path appHome;
	private final Path reposFile;

	private Map<String, Pair<Repository, RepositoryResource>> repositories = Maps.newHashMap();

	private GitExecutor gitExecutor;

	public PusherService(final String appHome, GitExecutor gitExecutor) {
		this.gitExecutor = Preconditions.checkNotNull(gitExecutor);
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

	void reloadRepositories() {
		if (Files.isRegularFile(this.reposFile)) {
			try (final InputStream is = Files.newInputStream(this.reposFile)) {
				final ObjectMapper mapper = new ObjectMapper();
				List<RepositoryResource> reposList = mapper.readValue(is, new TypeReference<List<RepositoryResource>>(){});
				reposList.forEach(res -> this.repositories.put(res.getName(), Pair.of(this.gitExecutor.openRepository(Paths.get(res.getPath(), GitExecutor.GIT_DIR_NAME)), res)));
			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	void saveRepositories() {
		try (OutputStream os = Files.newOutputStream(this.reposFile)) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(os, this.getRepositories());
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
		return this.repositories.entrySet().stream().map(e -> e.getValue().getRight()).collect(Collectors.toList());
	}

	@Override
	public RepositoryResource getRepository(final String name) {
		return this.checkReposExist(name, true).getRight();
	}

	@Override
	public RepositoryResource createRepository(final RepositoryResource repository) {
		this.checkReposExist(repository.getName(), false);
		this.repositories.put(repository.getName(),
				Pair.of(this.gitExecutor.openAndCreateRepository(Paths.get(repository.getPath())), repository));
		return repository;
	}

	@Override
	public void deleteRepository(final String name) {
		this.checkReposExist(name, true);
		this.repositories.remove(name);
	}

	@Override
	public RepositoryResource updateRepository(final RepositoryResource repository) {
		this.repositories.put(repository.getName(),
				Pair.of(this.checkReposExist(repository.getName(), true).getLeft(), repository));
		return repository;
	}

	@Override
	public void triggerRepository(final String name) {
		// TODO Do the trigger-stuff here.....
	}

	private Pair<Repository, RepositoryResource> checkReposExist(String name, boolean mustExist) {
		if (mustExist) {
			return Preconditions.checkNotNull(this.repositories.get(name), "Repository does not exist. Repository=" + name);
		}
		else {
			Preconditions.checkState(this.repositories.get(name) == null, "Repository does already exist. Repository=" + name);
			return null;
		}
	}
}
