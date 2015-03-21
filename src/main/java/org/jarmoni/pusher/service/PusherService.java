package org.jarmoni.pusher.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Repository;
import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.resource.RepositoryResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

public class PusherService implements IPusherService {

	public static final String REPOS_FILE_NAME = "repos.json";

	private static final Logger LOG = LoggerFactory.getLogger(PusherService.class);

	private Path appHome;
	private final Path reposFile;

	private final Map<String, Pair<Repository, RepositoryResource>> repositories = Maps.newHashMap();

	private final GitExecutor gitExecutor;

	private final Timer timer;

	public PusherService(final String appHome, final GitExecutor gitExecutor) {
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
		this.timer = new Timer("GitTimer");
	}

	// Lifecycle-method
	public void init() {
		this.reloadRepositories();
		this.timer.scheduleAtFixedRate(new GitTimerTask(), 0L, 60 * 1000L);
	}

	public void stop() {
		this.timer.cancel();
	}

	void reloadRepositories() {
		this.unregisterRepositories();
		if (Files.isRegularFile(this.reposFile)) {
			try (final InputStream is = Files.newInputStream(this.reposFile)) {
				final ObjectMapper mapper = new ObjectMapper();
				final List<RepositoryResource> reposList = mapper.readValue(is, new TypeReference<List<RepositoryResource>>(){});
				reposList.forEach(res -> this.repositories.put(res.getName(), Pair.of(this.gitExecutor.openRepository(Paths.get(res.getPath(), GitExecutor.GIT_DIR_NAME)), res))
						);
			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	void unregisterRepositories() {
		try {
			LOG.info("disabled atm...");
			// unregister all repositories: remove watcher, clear list....
		}
		catch (final Throwable t) {
			Throwables.propagate(t);
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

	private Pair<Repository, RepositoryResource> checkReposExist(final String name, final boolean mustExist) {
		if (mustExist) {
			return Preconditions.checkNotNull(this.repositories.get(name), "Repository does not exist. Repository=" + name);
		}
		else {
			Preconditions.checkState(this.repositories.get(name) == null, "Repository does already exist. Repository=" + name);
			return null;
		}
	}

	public class GitTimerTask extends TimerTask {

		@Override
		public void run() {
			repositories.entrySet().forEach(p -> {
				try {
					gitExecutor.commitChanges(p.getValue().getLeft(), p.getValue().getRight());
				}
				catch(final Exception ex) {
					LOG.warn("Exception occured", ex);
				}
			});

		}
	}
}
